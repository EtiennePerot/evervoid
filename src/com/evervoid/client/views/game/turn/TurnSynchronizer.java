package com.evervoid.client.views.game.turn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evervoid.client.views.solar.UIShip;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Action;
import com.evervoid.state.action.Turn;
import com.evervoid.state.action.ship.JumpShipIntoPortal;
import com.evervoid.state.action.ship.MoveShip;
import com.evervoid.state.action.ship.ShootShip;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Ship;

public class TurnSynchronizer
{
	private final Map<Ship, Set<UIShip>> aShips = new HashMap<Ship, Set<UIShip>>();
	private final EVGameState aState;
	private final Set<UIShip> aStep1CombatShips = new HashSet<UIShip>();
	private final Set<UIShip> aStep2JumpingShips = new HashSet<UIShip>();
	private final Set<UIShip> aStep3MovingShips = new HashSet<UIShip>();
	private final Turn aTurn;

	public TurnSynchronizer(final EVGameState state, final Turn turn)
	{
		aState = state;
		aTurn = turn.clone();
	}

	private void commitAction(final Action act)
	{
		// Remove Action from Turn since we're gonna commit it now and manually
		aTurn.delAction(act);
		// Then actually commit it
		aState.commitAction(act);
	}

	public void execute(final Runnable callback)
	{
		// TODO: Commit other shit before movement
		// Step 1: Combat
		step1Init(new Runnable()
		{
			@Override
			public void run()
			{
				// Step 2: Jumps
				step2Init(new Runnable()
				{
					@Override
					public void run()
					{
						// Step 3: movement
						step3Init(new Runnable()
						{
							@Override
							public void run()
							{
								// Commit all the rest
								aState.commitTurn(aTurn);
								if (callback != null) {
									callback.run();
								}
							}
						});
					}
				});
			}
		});
	}

	public void registerShip(final Ship ship, final UIShip uiship)
	{
		if (!aShips.containsKey(ship)) {
			aShips.put(ship, new HashSet<UIShip>());
		}
		aShips.get(ship).add(uiship);
	}

	private void step1Init(final Runnable callback)
	{
		final List<Action> actions = aTurn.getActionsOfType(ShootShip.class.getName());
		if (actions.isEmpty()) { // If no combat action, just run the callback now
			callback.run();
			return;
		}
		for (final Action act : actions) {
			final ShootShip shoot = (ShootShip) act;
			for (final UIShip uiship : aShips.get(shoot.getShip())) {
				aStep1CombatShips.add(uiship);
				uiship.shoot(shoot.getTarget().getLocation(), new Runnable()
				{
					@Override
					public void run()
					{
						step1ShipDoneShooting(uiship, callback);
					}
				});
			}
		}
	}

	private void step1ShipDoneShooting(final UIShip uiship, final Runnable callback)
	{
		aStep1CombatShips.remove(uiship);
		if (aStep1CombatShips.isEmpty()) {
			callback.run();
		}
	}

	private void step2Init(final Runnable callback)
	{
		final List<Action> jumps = aTurn.getActionsOfType(JumpShipIntoPortal.class.getName());
		if (jumps.isEmpty()) {
			if (callback != null) {
				callback.run();
			}
			return;
		}
		for (final Action act : jumps) {
			final JumpShipIntoPortal jump = (JumpShipIntoPortal) act;
			for (final UIShip uiship : aShips.get(jump.getShip())) {
				aStep2JumpingShips.add(uiship);
				uiship.jump(jump.getUnderlyingMove().getSamplePath(), jump.getPortal().getLocation(), new Runnable()
				{
					@Override
					public void run()
					{
						step2ShipDoneJumping(uiship, callback);
					}
				});
			}
		}
	}

	private void step2ShipDoneJumping(final UIShip uiship, final Runnable callback)
	{
		aStep2JumpingShips.remove(uiship);
		if (aStep2JumpingShips.isEmpty()) {
			callback.run();
		}
	}

	private void step3Init(final Runnable callback)
	{
		// Step 3: Movement; this is the tricky part
		final List<BagOfMoves> moveBags = new ArrayList<BagOfMoves>();
		for (final Action act : aTurn.getActionsOfType(MoveShip.class.getName())) {
			final MoveShip move = (MoveShip) act;
			if (moveBags.isEmpty()) {
				moveBags.add(new BagOfMoves(move));
			}
			else {
				for (final BagOfMoves bag : moveBags) {
					if (!bag.addMoveShip(move)) {
						moveBags.add(new BagOfMoves(move));
						break;
					}
				}
			}
			// Commit move NOW; this won't have any UI effect, but will affect the state so that we can compute new
			// paths
			// properly, with the new ship locations
			commitAction(move);
		}
		// Do an extra pass to check if we can merge bags of moves
		boolean canMerge = true;
		BagOfMoves mergeBag1 = null;
		BagOfMoves mergeBag2 = null;
		while (canMerge) {
			canMerge = false;
			for (final BagOfMoves bag1 : moveBags) {
				for (final BagOfMoves bag2 : moveBags) {
					if (bag1.collidesWith(bag2)) {
						mergeBag1 = bag1;
						mergeBag2 = bag2;
						canMerge = true;
						break;
					}
				}
				if (canMerge) {
					break;
				}
			}
			if (canMerge) {
				mergeBag1.mergeWith(mergeBag2);
				moveBags.remove(mergeBag2);
			}
		}
		// Now commit movement
		step3MoveStep(moveBags, callback);
	}

	/**
	 * Recursive call for movement
	 * 
	 * @param moves
	 *            The moves left to do
	 * @param callback
	 *            Callback to run when all is done
	 */
	private void step3MoveStep(final List<BagOfMoves> moves, final Runnable callback)
	{
		aStep3MovingShips.clear();
		final List<MoveShip> currentBatch = new ArrayList<MoveShip>(moves.size());
		for (final BagOfMoves bag : moves) {
			final MoveShip move = bag.getOneMove();
			if (move != null) {
				currentBatch.add(move);
			}
		}
		if (currentBatch.isEmpty()) { // If no moves, just call the callback
			callback.run();
			return;
		}
		final Runnable nextMove = new Runnable()
		{
			@Override
			public void run()
			{
				step3MoveStep(moves, callback);
			}
		};
		for (final MoveShip move : currentBatch) {
			final Ship ship = move.getShip();
			for (final UIShip uiship : aShips.get(ship)) {
				aStep3MovingShips.add(uiship);
				uiship.smoothMoveTo(new ArrayList<GridLocation>(move.getFinalPath().getPath()), new Runnable()
				{
					@Override
					public void run()
					{
						step3ShipDoneMoving(uiship, nextMove);
					}
				});
			}
		}
	}

	private void step3ShipDoneMoving(final UIShip uiship, final Runnable callback)
	{
		aStep3MovingShips.remove(uiship);
		if (aStep3MovingShips.isEmpty()) {
			callback.run();
		}
	}
}

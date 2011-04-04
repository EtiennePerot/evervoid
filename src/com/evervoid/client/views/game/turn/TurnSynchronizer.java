package com.evervoid.client.views.game.turn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evervoid.client.graphics.geometry.FrameTimer;
import com.evervoid.client.views.solar.UIShip;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Action;
import com.evervoid.state.action.Turn;
import com.evervoid.state.action.planet.RegeneratePlanet;
import com.evervoid.state.action.ship.BombPlanet;
import com.evervoid.state.action.ship.CapturePlanet;
import com.evervoid.state.action.ship.EnterCargo;
import com.evervoid.state.action.ship.JumpShipIntoPortal;
import com.evervoid.state.action.ship.LeaveCargo;
import com.evervoid.state.action.ship.MoveShip;
import com.evervoid.state.action.ship.RegenerateShip;
import com.evervoid.state.action.ship.ShipAction;
import com.evervoid.state.action.ship.ShootShip;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Ship;

public class TurnSynchronizer
{
	private final Map<Ship, Set<UIShip>> aShips = new HashMap<Ship, Set<UIShip>>();
	private final EVGameState aState;
	private final Set<UIShip> aStep2CombatShips = new HashSet<UIShip>();
	private final Set<UIShip> aStep3DockingShips = new HashSet<UIShip>();
	private final Set<UIShip> aStep5JumpingShips = new HashSet<UIShip>();
	private final Set<UIShip> aStep6CapturingShips = new HashSet<UIShip>();
	private final Set<UIShip> aStep7MovingShips = new HashSet<UIShip>();
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
		// !WARNING!: If you need to modify the order of actions, do not forget to edit res/action_order.txt and EVGameEngine to
		// reflect the changes.
		// Warning, this looks insane but makes some level of sense
		// 1: Regeneration (Ships, planets)
		for (final Action regen : aTurn.getActionsOfType(RegenerateShip.class, RegeneratePlanet.class)) {
			commitAction(regen);
		}
		// 2: Combat (Shooting, bombing)
		step2Combat(new Runnable()
		{
			@Override
			public void run()
			{
				// 3: Docking ships
				step3Docking(new Runnable()
				{
					@Override
					public void run()
					{
						// 4: Unloading ships
						step4Unloading(new Runnable()
						{
							@Override
							public void run()
							{
								// 5: Jumps
								step5Jumps(new Runnable()
								{
									@Override
									public void run()
									{
										// 6: Capturing planets
										step6Capture(new Runnable()
										{
											@Override
											public void run()
											{
												// 7: Movement
												step7Move(new Runnable()
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

	private void step2Combat(final Runnable callback)
	{
		final List<Action> actions = aTurn.getActionsOfType(ShootShip.class, BombPlanet.class).getActions();
		if (actions.isEmpty()) { // If no combat action, just run the callback now
			callback.run();
			return;
		}
		// Need 2 loops to prevent concurrent modification
		for (final Action act : actions) {
			aStep2CombatShips.addAll(aShips.get(((ShipAction) act).getShip()));
		}
		for (final Action act : actions) {
			GridLocation loc = null;
			if (act instanceof ShootShip) {
				loc = ((ShootShip) act).getTarget().getLocation();
			}
			else if (act instanceof BombPlanet) {
				loc = ((BombPlanet) act).getTarget().getLocation();
			}
			if (loc != null) {
				for (final UIShip uiship : aShips.get(((ShipAction) act).getShip())) {
					uiship.shoot(loc, new Runnable()
					{
						@Override
						public void run()
						{
							step2ShipDoneShooting(uiship, callback);
						}
					});
				}
			}
			commitAction(act);
		}
	}

	private void step2ShipDoneShooting(final UIShip uiship, final Runnable callback)
	{
		aStep2CombatShips.remove(uiship);
		if (aStep2CombatShips.isEmpty()) {
			callback.run();
		}
	}

	private void step3Docking(final Runnable callback)
	{
		final List<Action> enterCargos = aTurn.getActionsOfType(EnterCargo.class).getActions();
		if (enterCargos.isEmpty()) {
			if (callback != null) {
				callback.run();
			}
			return;
		}
		// Need 2 loops to prevent concurrent modification
		for (final Action act : enterCargos) {
			aStep3DockingShips.addAll(aShips.get(((EnterCargo) act).getShip()));
		}
		for (final Action act : enterCargos) {
			final EnterCargo enter = (EnterCargo) act;
			for (final UIShip uiship : aShips.get(enter.getShip())) {
				uiship.enterCargo(enter.getUnderlyingMove().getSamplePath(), enter.getTarget().getLocation(), new Runnable()
				{
					@Override
					public void run()
					{
						step3ShipDoneDocking(uiship, callback);
					}
				});
			}
			commitAction(act);
		}
	}

	private void step3ShipDoneDocking(final UIShip uiship, final Runnable callback)
	{
		aStep3DockingShips.remove(uiship);
		if (aStep3DockingShips.isEmpty()) {
			callback.run();
		}
	}

	private void step4Unloading(final Runnable callback)
	{
		final List<Action> leaveCargos = aTurn.getActionsOfType(LeaveCargo.class).getActions();
		for (final Action act : leaveCargos) {
			commitAction(act);
		}
		if (leaveCargos.isEmpty()) {
			// Call callback directly
			callback.run();
		}
		else {
			// Else, call it when all ships have appeared
			new FrameTimer(callback, UIShip.sUIShipAppearTime, 1).start();
		}
	}

	private void step5Jumps(final Runnable callback)
	{
		final List<Action> jumps = aTurn.getActionsOfType(JumpShipIntoPortal.class).getActions();
		if (jumps.isEmpty()) {
			if (callback != null) {
				callback.run();
			}
			return;
		}
		// Need 2 loops to prevent concurrent modification
		for (final Action act : jumps) {
			aStep5JumpingShips.addAll(aShips.get(((JumpShipIntoPortal) act).getShip()));
		}
		for (final Action act : jumps) {
			final JumpShipIntoPortal jump = (JumpShipIntoPortal) act;
			for (final UIShip uiship : aShips.get(jump.getShip())) {
				uiship.jump(jump.getUnderlyingMove().getSamplePath(), jump.getPortal().getLocation(), new Runnable()
				{
					@Override
					public void run()
					{
						step5ShipDoneJumping(uiship, callback);
					}
				});
			}
			commitAction(act);
		}
	}

	private void step5ShipDoneJumping(final UIShip uiship, final Runnable callback)
	{
		aStep5JumpingShips.remove(uiship);
		if (aStep5JumpingShips.isEmpty()) {
			callback.run();
		}
	}

	private void step6Capture(final Runnable callback)
	{
		final List<Action> captures = aTurn.getActionsOfType(CapturePlanet.class).getActions();
		if (captures.isEmpty()) {
			if (callback != null) {
				callback.run();
			}
			return;
		}
		// Need 2 loops to prevent concurrent modification
		for (final Action act : captures) {
			aStep6CapturingShips.addAll(aShips.get(((CapturePlanet) act).getShip()));
		}
		for (final Action act : captures) {
			final CapturePlanet capture = (CapturePlanet) act;
			for (final UIShip uiship : aShips.get(capture.getShip())) {
				uiship.capture(capture.getUnderlyingMove().getSamplePath(), capture.getTarget(), new Runnable()
				{
					@Override
					public void run()
					{
						step6ShipDoneCapturing(uiship, callback);
					}
				});
			}
			commitAction(act);
		}
	}

	private void step6ShipDoneCapturing(final UIShip uiship, final Runnable callback)
	{
		aStep6CapturingShips.remove(uiship);
		if (aStep6CapturingShips.isEmpty()) {
			callback.run();
		}
	}

	private void step7Move(final Runnable callback)
	{
		final List<BagOfMoves> moveBags = new ArrayList<BagOfMoves>();
		for (final Action act : aTurn.getActionsOfType(MoveShip.class)) {
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
			// paths properly, with the new ship locations
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
		step7MoveStep(moveBags, callback);
	}

	/**
	 * Recursive call for movement
	 * 
	 * @param moves
	 *            The moves left to do
	 * @param callback
	 *            Callback to run when all is done
	 */
	private void step7MoveStep(final List<BagOfMoves> moves, final Runnable callback)
	{
		aStep7MovingShips.clear();
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
				step7MoveStep(moves, callback);
			}
		};
		// Need 2 loops to prevent concurrent modification
		for (final MoveShip move : currentBatch) {
			aStep7MovingShips.addAll(aShips.get(move.getShip()));
		}
		for (final MoveShip move : currentBatch) {
			final Ship ship = move.getShip();
			for (final UIShip uiship : aShips.get(ship)) {
				uiship.smoothMoveTo(new ArrayList<GridLocation>(move.getFinalPath().getPath()), new Runnable()
				{
					@Override
					public void run()
					{
						step7ShipDoneMoving(uiship, nextMove);
					}
				});
			}
		}
	}

	private void step7ShipDoneMoving(final UIShip uiship, final Runnable callback)
	{
		aStep7MovingShips.remove(uiship);
		if (aStep7MovingShips.isEmpty()) {
			callback.run();
		}
	}
}

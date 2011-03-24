package com.evervoid.client.views.game;

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
import com.evervoid.state.action.ship.MoveShip;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.prop.Ship;
import com.evervoid.state.prop.ShipPath;

public class TurnSynchronizer
{
	private class BagOfMoves
	{
		private final List<MoveShip> aMoves = new ArrayList<MoveShip>();
		private final Set<Point> aPoints;

		private BagOfMoves(final MoveShip initial)
		{
			aPoints = new HashSet<Point>(initial.getFinalPath().getPoints());
			aMoves.add(initial);
		}

		private boolean addMoveShip(final MoveShip action)
		{
			final ShipPath finalPath = action.getFinalPath();
			if (finalPath.collidesWith(aPoints)) {
				aPoints.addAll(finalPath.getPoints());
				aMoves.add(action);
				return true;
			}
			return false;
		}

		private MoveShip getOneMove()
		{
			if (aMoves.isEmpty()) {
				return null;
			}
			return aMoves.remove(0);
		}
	}

	private final Set<UIShip> aMovingShips = new HashSet<UIShip>();
	private final Map<Ship, Set<UIShip>> aShips = new HashMap<Ship, Set<UIShip>>();
	private final EVGameState aState;
	private final Turn aTurn;

	TurnSynchronizer(final EVGameState state, final Turn turn)
	{
		aState = state;
		aTurn = turn.clone();
	}

	private void commitMoves(final List<BagOfMoves> moves, final Runnable callback)
	{
		aMovingShips.clear();
		final List<MoveShip> currentBatch = new ArrayList<MoveShip>(moves.size());
		for (final BagOfMoves bag : moves) {
			final MoveShip move = bag.getOneMove();
			if (move != null) {
				currentBatch.add(move);
			}
		}
		if (currentBatch.isEmpty()) {
			callback.run();
			return;
		}
		final Runnable nextMove = new Runnable()
		{
			@Override
			public void run()
			{
				commitMoves(moves, callback);
			}
		};
		for (final MoveShip move : currentBatch) {
			final Ship ship = move.getShip();
			for (final UIShip uiship : aShips.get(ship)) {
				aMovingShips.add(uiship);
				uiship.smoothMoveTo(new ArrayList<GridLocation>(move.getFinalPath().getPath()), new Runnable()
				{
					@Override
					public void run()
					{
						shipDoneMoving(uiship, nextMove);
					}
				});
			}
		}
	}

	void execute(final Runnable callback)
	{
		// TODO: Commit combat and other shit before movement
		final List<BagOfMoves> moveSets = new ArrayList<BagOfMoves>();
		for (final Action act : aTurn.getActionsOfType("MoveShip")) {
			final MoveShip move = (MoveShip) act;
			if (moveSets.isEmpty()) {
				moveSets.add(new BagOfMoves(move));
			}
			else {
				for (final BagOfMoves bag : moveSets) {
					if (!bag.addMoveShip(move)) {
						moveSets.add(new BagOfMoves(move));
						break;
					}
				}
			}
			aTurn.delAction(move);
			aState.commitAction(move); // Commit move NOW; this won't have any UI effect
		}
		// Commit all the rest
		aState.commitTurn(aTurn);
		// Now commit movement
		commitMoves(moveSets, callback);
	}

	public void registerShip(final Ship ship, final UIShip uiship)
	{
		if (!aShips.containsKey(ship)) {
			aShips.put(ship, new HashSet<UIShip>());
		}
		aShips.get(ship).add(uiship);
	}

	private void shipDoneMoving(final UIShip uiship, final Runnable callback)
	{
		aMovingShips.remove(uiship);
		if (aMovingShips.isEmpty()) {
			callback.run();
		}
	}
}

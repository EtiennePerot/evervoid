package com.evervoid.client.views.game.turn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.ship.MoveShip;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.prop.ShipPath;

/**
 * Internal data structure used to represent a "loose" bunch of Points covered by moves.
 */
class BagOfMoves
{
	private final SolarSystem aContainer;
	private final List<MoveShip> aMoves = new ArrayList<MoveShip>();
	private final Set<Point> aPoints;

	BagOfMoves(final MoveShip initial)
	{
		aPoints = new HashSet<Point>(initial.getFinalPath().getPoints());
		aMoves.add(initial);
		aContainer = (SolarSystem) initial.getShip().getContainer();
	}

	boolean addMoveShip(final MoveShip action)
	{
		final ShipPath finalPath = action.getFinalPath();
		if (finalPath.collidesWith(aPoints, aContainer)) {
			aPoints.addAll(finalPath.getPoints());
			aMoves.add(action);
			return true;
		}
		return false;
	}

	boolean collidesWith(final BagOfMoves other)
	{
		if (equals(other) || !aContainer.equals(other.aContainer)) { // Can't collide with self or with another container
			return false;
		}
		if (other.aPoints.size() < aPoints.size()) { // If other has less points
			return other.collidesWith(this); // Then switch around the order of comparison
		}
		// Else, the current bag is the smaller one
		for (final Point p : aPoints) {
			if (other.aPoints.contains(p)) {
				return true;
			}
		}
		return false;
	}

	MoveShip getOneMove()
	{
		if (aMoves.isEmpty()) {
			return null;
		}
		return aMoves.remove(0);
	}

	void mergeWith(final BagOfMoves other)
	{
		for (final MoveShip act : other.aMoves) {
			aMoves.add(act);
		}
		other.aMoves.clear();
		for (final Point p : other.aPoints) {
			aPoints.add(p);
		}
		other.aPoints.clear();
	}
}
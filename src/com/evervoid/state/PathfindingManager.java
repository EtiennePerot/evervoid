package com.evervoid.state;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.prop.Ship;

public class PathfindingManager
{
	public PathfindingManager()
	{
	}

	private Set<Point> getNeighbours(final Point p)
	{
		final Set<Point> directNeighbours = new HashSet<Point>();
		final Point east = new Point(p.x + 1, p.y);
		final Point west = new Point(p.x - 1, p.y);
		final Point north = new Point(p.x, p.y + 1);
		final Point south = new Point(p.x, p.y - 1);
		directNeighbours.add(south);
		directNeighbours.add(north);
		directNeighbours.add(west);
		directNeighbours.add(east);
		return directNeighbours;
	}

	/**
	 * Returns a list of points where a ship can move to.
	 * 
	 * @param pShip
	 *            A ship located in a solarSystem.
	 * @param pSolarSystem
	 *            The solar system containing the ship.
	 * @return A list of points where the specified ship can move within the solar system.
	 */
	public Set<Point> getValidDestinations(final SolarSystem pSolarSystem, final Ship pShip)
	{
		final Point shipOrigin = pShip.getLocation().origin;
		final Dimension shipDimension = pShip.getLocation().dimension;
		final Set<Point> graphFrontier = new HashSet<Point>();
		final Set<Point> newFrontier = new HashSet<Point>();
		final Set<Point> validDestinations = new HashSet<Point>();
		graphFrontier.addAll(getNeighbours(shipOrigin));
		// Implementation of a limited-depth breadth-first search.
		for (int i = 0; i < pShip.getSpeed(); i++) {
			// Traverse all the points contained in the frontier.
			for (final Point p : graphFrontier) {
				if (!pSolarSystem.isOccupied(new GridLocation(p, shipDimension))) {
					// Point is not occupied nor already known as valid.
					validDestinations.add(p);
					// Add the neighbors to the new frontier.
					newFrontier.addAll(getNeighbours(p));
				}
			}
			/*
			 * Remove all already known points from the new frontier, clear the old frontier and replace with new frontier.
			 */
			newFrontier.removeAll(validDestinations);
			graphFrontier.clear();
			graphFrontier.addAll(newFrontier);
			newFrontier.clear();
		}
		validDestinations.remove(shipOrigin);
		return validDestinations;
	}
}

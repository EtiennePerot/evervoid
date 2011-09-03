package com.evervoid.state.prop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.ship.MoveShip;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;

/**
 * A ShipPath represents the path a {@link Ship} takes along a {@link SolarSystem} while moving. This path must not intersect
 * with any other {@link Prop}s in order to be executed by a {@link MoveShip} action.
 */
public class ShipPath
{
	/**
	 * The ordered GridLocations associated with this path.
	 */
	private final List<GridLocation> aPath;
	/**
	 * Set of Points traversed by this path
	 */
	private final Set<Point> aPoints;
	/**
	 * The Solar System in which the path will be executed.
	 */
	private final SolarSystem aSolarSystem;

	/**
	 * @param origin
	 *            The path's starting locations.
	 * @param destination
	 *            The path's destination.
	 * @param path
	 *            The ordered list of GridLocations at which the ship must stop to rotate.
	 * @param solarsystem
	 *            The SolarSystem to which this ShipPath will belong.
	 */
	public ShipPath(final GridLocation origin, final GridLocation destination, final List<GridLocation> path,
			final SolarSystem solarsystem)
	{
		if (path == null || path.isEmpty()) {
			System.err.println("Warning: Empty path in ShipPath! Defaulting to straight line.");
			aPath = new ArrayList<GridLocation>(2);
			aPath.add(origin);
			aPath.add(destination);
		}
		else {
			aPath = path;
		}
		aSolarSystem = solarsystem;
		aPoints = new HashSet<Point>();
		GridLocation previous = origin;
		final Pathfinder pathFinder = new Pathfinder();
		for (final GridLocation loc : aPath) {
			for (final GridLocation subloc : pathFinder.getDirectRoute(previous.origin, loc.origin, loc.dimension)) {
				aPoints.addAll(subloc.getPoints());
			}
			previous = loc;
		}
	}

	/**
	 * Internal constructor used for cloning
	 * 
	 * @param origin
	 *            The origin of the path
	 */
	private ShipPath(final ShipPath origin)
	{
		aPath = new ArrayList<GridLocation>(origin.aPath);
		aPoints = new HashSet<Point>(origin.aPoints);
		aSolarSystem = origin.aSolarSystem;
	}

	@Override
	public ShipPath clone()
	{
		return new ShipPath(this);
	}

	/**
	 * Checks for collisions on a point by point basis.
	 * 
	 * @param points
	 *            The points to check for collisions.
	 * @param solarSystem
	 *            The SolarSystem in which collisions are being checked for
	 * @return True if no collisions were detected between this set of points and all points on the path.
	 */
	public boolean collidesWith(final Set<Point> points, final SolarSystem solarSystem)
	{
		if (getSolarSystem().equals(solarSystem)) {
			// no point comparing if we're not in the same solar system
			for (final Point p : aPoints) {
				if (points.contains(p)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks for collisions by comparing every point on the parameter path to those on the local path.
	 * 
	 * @param other
	 *            The path to compare to.
	 * @return Whether the two paths collide.
	 */
	public boolean collidesWith(final ShipPath other)
	{
		if (other == null) {
			return false;
		}
		if (other.aPoints.size() < aPoints.size()) { // If other has less points
			return other.collidesWith(this); // Then switch order of comparison
		}
		// Else, do the comparison (the current GridPath has less points than the other)
		return collidesWith(other.getPoints(), other.getSolarSystem());
	}

	/**
	 * @return A list of all Grid Locations the ship will turn at. The locations are ordered according to the path followed.
	 */
	public List<GridLocation> getPath()
	{
		return aPath;
	}

	/**
	 * @return The set of all points the ship will hit on the way along the path.
	 */
	public Set<Point> getPoints()
	{
		return aPoints;
	}

	/**
	 * @return The Solar System to which this path belongs.
	 */
	public SolarSystem getSolarSystem()
	{
		return aSolarSystem;
	}
}

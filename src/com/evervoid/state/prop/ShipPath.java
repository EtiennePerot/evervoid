package com.evervoid.state.prop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;

public class ShipPath
{
	private final List<GridLocation> aPath;
	/**
	 * Set of Points traversed by this path
	 */
	private final Set<Point> aPoints;

	public ShipPath(final GridLocation origin, final List<GridLocation> path)
	{
		aPath = path;
		if (aPath.isEmpty()) {
			System.err.println("Warning: Empty path in ShipPath!");
		}
		aPoints = new HashSet<Point>();
		GridLocation previous = origin;
		final Pathfinder pathFinder = new Pathfinder();
		for (final GridLocation loc : path) {
			for (final GridLocation subloc : pathFinder.getDirectRoute(previous.origin, loc.origin, loc.dimension)) {
				aPoints.addAll(subloc.getPoints());
			}
			previous = loc;
		}
	}

	/**
	 * Internal constructor used for cloning
	 */
	private ShipPath(final ShipPath origin)
	{
		aPath = new ArrayList<GridLocation>(origin.aPath);
		aPoints = new HashSet<Point>(origin.aPoints);
	}

	@Override
	public ShipPath clone()
	{
		return new ShipPath(this);
	}

	public boolean collidesWith(final Set<Point> points)
	{
		for (final Point p : aPoints) {
			if (points.contains(p)) {
				return true;
			}
		}
		return false;
	}

	public boolean collidesWith(final ShipPath other)
	{
		if (other == null) {
			return false;
		}
		if (other.aPoints.size() < aPoints.size()) { // If other has less points
			return other.collidesWith(this); // Then switch order of comparison
		}
		// Else, do the comparison (the current GridPath has less points than the other)
		return collidesWith(other.getPoints());
	}

	public List<GridLocation> getPath()
	{
		return aPath;
	}

	public Set<Point> getPoints()
	{
		return aPoints;
	}
}

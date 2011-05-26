package com.evervoid.state.geometry;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.utils.MathUtils;
import com.jme3.math.FastMath;

/**
 * A simple immutable representation of a point at (x, y). Warning: This class is not simply used for grid-related purposes, as
 * such it should not contain grid related dependencies (GridLocation should be used instead).
 */
public final class Point implements Cloneable, Jsonable
{
	/**
	 * The Point's x coordinate.
	 */
	public int x;
	/**
	 * The Point's y coordinate.
	 */
	public int y;

	/**
	 * Creates a Point at the origin.
	 */
	public Point()
	{
		this(0, 0);
	}

	/**
	 * Creates a point at ( floor(x), floor(y) ).
	 * 
	 * @param x
	 *            The X coordinate of the Point.
	 * @param y
	 *            The y coordinate of the Point.
	 */
	public Point(final float x, final float y)
	{
		this((int) x, (int) y);
	}

	/**
	 * Creates a point at ( x, y ).
	 * 
	 * @param x
	 *            The X coordinate of the Point.
	 * @param y
	 *            The y coordinate of the Point.
	 */
	public Point(final int x, final int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Creates a Point from the contents of the Json.
	 * 
	 * @param j
	 *            The Json containing the necessary information.
	 */
	public Point(final Json j)
	{
		this(j.getIntAttribute("x"), j.getIntAttribute("y"));
	}

	/**
	 * Creates a new Point translated from the original by the given x and y values.
	 * 
	 * @param xOffset
	 *            The x Offset.
	 * @param yOffset
	 *            The y Offset.
	 * @return The new translated Point.
	 */
	public Point add(final int xOffset, final int yOffset)
	{
		return new Point(x + xOffset, y + yOffset);
	}

	/**
	 * Creates a new Point translated from the original by the vector joining the parameter Point to the origin.
	 * 
	 * @param point
	 *            The Point offset.
	 * @return The new translated Point.
	 */
	public Point add(final Point point)
	{
		return add(point.x, point.y);
	}

	@Override
	public Point clone()
	{
		return new Point(x, y);
	}

	/**
	 * @return A new Point with the x and y coordinates clamped between their respective bounds.
	 */
	public Point constrain(final int minX, final int minY, final int maxX, final int maxY)
	{
		return new Point(MathUtils.clampInt(minX, x, maxX), MathUtils.clampInt(minY, y, maxY));
	}

	/**
	 * @return The Euclidian distance between the two Points.
	 */
	public float distanceTo(final Point other)
	{
		return FastMath.sqrt(FastMath.sqr(x - other.x) + FastMath.sqr(y - other.y));
	}

	/**
	 * @param other
	 *            Other Object to compare to.
	 * @return True if the other object is the Point and has the same coordinates, false otherwise.
	 */
	@Override
	public boolean equals(final Object other)
	{
		if (super.equals(other)) {
			return true;
		}
		if (other == null || !other.getClass().equals(getClass())) {
			return false;
		}
		final Point p = (Point) other;
		return p.x == x && p.y == y;
	}

	/**
	 * @return The Point in the list that is closest to this one as determined by the Manhattan distance.
	 */
	public Point getClosestTo(final Iterable<Point> points)
	{
		int minDistance = Integer.MAX_VALUE;
		Point minPoint = null;
		for (final Point p : points) {
			final int distance = getManhattanDistance(p);
			if (distance < minDistance) {
				minDistance = distance;
				minPoint = p;
			}
		}
		return minPoint.clone();
	}

	/**
	 * @return The Manhattan distance between the two Points.
	 */
	public int getManhattanDistance(final Point other)
	{
		return (int) (FastMath.abs(x - other.x) + FastMath.abs(y - other.y));
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	/**
	 * @return A new Point with the x and y values scaled by the scale factor.
	 */
	public Point scale(final float scaleFactor)
	{
		return new Point(x * scaleFactor, y * scaleFactor);
	}

	/**
	 * @return A new Point resulting from subtracting the x and y offsets from this Point.
	 */
	public Point subtract(final int xOffset, final int yOffset)
	{
		return new Point(x - xOffset, y - yOffset);
	}

	/**
	 * @return A new Point resulting from subtracting the parameter Point from this one.
	 */
	public Point subtract(final Point point)
	{
		return subtract(point.x, point.y);
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("x", x).setAttribute("y", y);
	}

	@Override
	public String toString()
	{
		return "[" + x + ", " + y + "]";
	}
}

package com.evervoid.state.geometry;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.jme3.math.FastMath;

/**
 * A simple two-integer point. Mainly used for grids. However, it is NOT recommended to use this class directly. Look at the
 * GridLocation class instead. Warning: Do not refactor or add grid-related dependencies, as this class is used for other
 * purposes. Extend GridLocation instead.
 */
public final class Point implements Cloneable, Jsonable
{
	public int x;
	public int y;

	public Point()
	{
		this(0, 0);
	}

	public Point(final int x, final int y)
	{
		this.x = x;
		this.y = y;
	}

	public Point(final Json j)
	{
		this(j.getIntAttribute("x"), j.getIntAttribute("y"));
	}

	public Point(final Point point)
	{
		x = point.x;
		y = point.y;
	}

	public Point add(final int x, final int y)
	{
		return new Point(this.x + x, this.y + y);
	}

	public Point add(final Point point)
	{
		return add(point.x, point.y);
	}

	@Override
	public Point clone()
	{
		return new Point(x, y);
	}

	public Point constrain(final int minX, final int minY, final int maxX, final int maxY)
	{
		return new Point(MathUtils.clampInt(minX, x, maxX), MathUtils.clampInt(minY, y, maxY));
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

	public int getManhattanDistance(final Point other)
	{
		return (int) (FastMath.abs(x - other.x) + FastMath.abs(y - other.y));
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	public Point subtract(final int x, final int y)
	{
		return new Point(this.x - x, this.y - y);
	}

	public Point subtract(final Point point)
	{
		return subtract(point.x, point.y);
	}

	@Override
	public Json toJson()
	{
		return new Json().setIntAttribute("x", x).setIntAttribute("y", y);
	}

	@Override
	public String toString()
	{
		return "[" + x + ", " + y + "]";
	}
}

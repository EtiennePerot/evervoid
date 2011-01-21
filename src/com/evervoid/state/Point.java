package com.evervoid.state;

import com.evervoid.client.graphics.geometry.MathUtils;

/**
 * A simple two-integer point. Mainly used for grids. However, it is NOT recommended to use this class directly. Look at the
 * GridLocation class instead. Warning: Do not refactor or add grid-related dependencies, as this class is used for other
 * purposes. Extend GridLocation instead.
 */
public final class Point
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

	public Point constrain(final int minX, final int minY, final int maxX, final int maxY)
	{
		return new Point(MathUtils.clampInt(minX, x, maxX), MathUtils.clampInt(minY, y, maxY));
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	public boolean sameAs(final Point other)
	{
		return x == other.x && y == other.y;
	}

	@Override
	public String toString()
	{
		return "[" + x + ", " + y + "]";
	}
}

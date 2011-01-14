package com.evervoid.client.graphics.geometry;

import com.evervoid.state.solar.Point;

public class GridPoint
{
	public int x;
	public int y;

	public GridPoint()
	{
		this(0, 0);
	}

	public GridPoint(final int x, final int y)
	{
		this.x = x;
		this.y = y;
	}

	public GridPoint(final Point point)
	{
		x = point.x;
		y = point.y;
	}

	public GridPoint add(final GridPoint point)
	{
		return add(point.x, point.y);
	}

	public GridPoint add(final int x, final int y)
	{
		return new GridPoint(this.x + x, this.y + y);
	}

	public GridPoint constrain(final int minX, final int minY, final int maxX, final int maxY)
	{
		return new GridPoint(Geometry.clampInt(minX, x, maxX), Geometry.clampInt(minY, y, maxY));
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	@Override
	public String toString()
	{
		return "[" + x + ", " + y + "]";
	}
}

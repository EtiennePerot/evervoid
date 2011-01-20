package com.evervoid.state;

import com.evervoid.gamedata.Dimension;

public class GridLocation
{
	public Dimension dimension;
	public Point origin;

	public GridLocation(final Point origin, final Dimension dimension)
	{
		this.origin = origin;
		this.dimension = dimension;
	}

	public boolean collides(final GridLocation other)
	{
		// TODO
		return false;
	}

	public int getHeight()
	{
		return dimension.height;
	}

	public int getWidth()
	{
		return dimension.width;
	}

	public int getX()
	{
		return origin.x;
	}

	public int getY()
	{
		return origin.y;
	}

	public boolean sameAs(final GridLocation other)
	{
		return origin.sameAs(other.origin) && dimension.sameAs(other.dimension);
	}
}

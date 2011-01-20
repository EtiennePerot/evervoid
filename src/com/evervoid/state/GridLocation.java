package com.evervoid.state;

import com.evervoid.gamedata.Dimension;

public class GridLocation
{
	public Dimension dimension;
	public Point origin;

	public GridLocation(final int x, final int y)
	{
		this(new Point(x, y), new Dimension());
	}

	public GridLocation(final Point origin)
	{
		this(origin, new Dimension());
	}

	public GridLocation(final Point origin, final Dimension dimension)
	{
		this.origin = origin;
		this.dimension = dimension;
	}

	public GridLocation add(final int x, final int y)
	{
		return add(new Point(x, y));
	}

	public GridLocation add(final Point point)
	{
		return new GridLocation(origin.add(point), dimension);
	}

	public boolean collides(final GridLocation other)
	{
		// TODO
		return false;
	}

	public GridLocation constrain(final int minX, final int minY, final int maxX, final int maxY)
	{
		return new GridLocation(origin.constrain(minX, minY, maxX - dimension.width, maxY - dimension.height), dimension);
	}

	public boolean fitsIn(final Dimension dimension)
	{
		return getX() + getWidth() <= dimension.width && getY() + getHeight() <= dimension.height;
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

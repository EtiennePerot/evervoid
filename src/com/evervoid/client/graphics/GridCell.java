package com.evervoid.client.graphics;

import com.evervoid.state.solar.Point;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class GridCell extends PlainRectangle
{
	private final Point aLocation;

	public GridCell(final int row, final int column, final Vector3f origin, final float width, final float height,
			final ColorRGBA fill)
	{
		this(new Point(column, row), origin, width, height, fill);
	}

	public GridCell(final Point location, final Vector3f origin, final float width, final float height, final ColorRGBA fill)
	{
		super(origin, width, height, fill);
		aLocation = location;
	}

	public Point getGridLocation()
	{
		return aLocation;
	}
}

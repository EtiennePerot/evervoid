package com.evervoid.client.graphics;

import com.evervoid.client.graphics.geometry.GridPoint;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class GridCell extends PlainRectangle
{
	private final GridPoint aLocation;

	public GridCell(final GridPoint location, final Vector3f origin, final float width, final float height,
			final ColorRGBA fill)
	{
		super(origin, width, height, fill);
		aLocation = location;
	}

	public GridCell(final int row, final int column, final Vector3f origin, final float width, final float height,
			final ColorRGBA fill)
	{
		this(new GridPoint(column, row), origin, width, height, fill);
	}

	public GridPoint getGridLocation()
	{
		return aLocation;
	}
}

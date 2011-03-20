package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.Grid;
import com.evervoid.client.ui.PlainLine;
import com.evervoid.state.geometry.GridLocation;
import com.jme3.math.ColorRGBA;

public class ActionLine extends PlainLine
{
	public ActionLine(final Grid grid, final GridLocation start, final GridLocation end, final float width,
			final ColorRGBA color)
	{
		super(grid.getCellCenter(start), grid.getCellCenter(end), 1f, color);
	}
}

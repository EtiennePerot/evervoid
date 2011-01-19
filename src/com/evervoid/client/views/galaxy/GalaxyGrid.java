package com.evervoid.client.views.galaxy;

import com.evervoid.client.graphics.Grid;
import com.jme3.math.ColorRGBA;

public class GalaxyGrid extends Grid
{
	public GalaxyGrid(final GalaxyView view)
	{
		super(48, 64, 64, 64, 1, new ColorRGBA(1f, 1f, 1f, 0.2f));
	}
}

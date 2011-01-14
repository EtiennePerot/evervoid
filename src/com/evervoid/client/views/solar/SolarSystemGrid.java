package com.evervoid.client.views.solar;


import com.evervoid.client.graphics.Grid;
import com.jme3.math.ColorRGBA;

public class SolarSystemGrid extends Grid
{
	private final SolarSystemView aSolarSystemView;

	public SolarSystemGrid(final SolarSystemView view)
	{
		super(48, 64, 64, 64, 1, new ColorRGBA(1f, 1f, 1f, 0.2f));
		aSolarSystemView = view;
	}

	@Override
	public void computeTransforms()
	{
		super.computeTransforms();
		aSolarSystemView.computeGridDimensions();
	}
}

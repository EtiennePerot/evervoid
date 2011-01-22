package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.Grid;
import com.evervoid.client.graphics.ShipTrailManager;
import com.evervoid.state.GridLocation;
import com.evervoid.state.SolarSystem;
import com.jme3.math.ColorRGBA;

public class SolarSystemGrid extends Grid
{
	private final SolarSystem aSolarSystem;
	private final SolarSystemView aSolarSystemView;
	private final ShipTrailManager aTrailManager = new ShipTrailManager(this);

	public SolarSystemGrid(final SolarSystemView view, final SolarSystem ss)
	{
		super(ss.getDimension(), 64, 64, 1, new ColorRGBA(1f, 1f, 1f, 0.2f));
		aSolarSystemView = view;
		aSolarSystem = ss;
	}

	@Override
	public void computeTransforms()
	{
		super.computeTransforms();
		aSolarSystemView.computeGridDimensions();
	}

	public GridLocation getSunLocation()
	{
		return aSolarSystem.getSunLocation();
	}

	public ShipTrailManager getTrailManager()
	{
		return aTrailManager;
	}
}

package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.Grid;
import com.evervoid.client.graphics.ShipTrailManager;
import com.evervoid.state.GridLocation;
import com.evervoid.state.SolarSystem;
import com.jme3.math.ColorRGBA;

/**
 * This class represents the grid displayed when in the solar system view.
 */
public class SolarSystemGrid extends Grid
{
	private final SolarSystem aSolarSystem;
	private final SolarSystemView aSolarSystemView;
	private final ShipTrailManager aTrailManager = new ShipTrailManager(this);

	/**
	 * Default constructor generating
	 * 
	 * @param view
	 *            The solar system view to generate the grid in.
	 * @param ss
	 *            The solar system represented by this grid.
	 */
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

	/**
	 * @return A GridLocation where the sun is located.
	 */
	public GridLocation getSunLocation()
	{
		return aSolarSystem.getSunLocation();
	}

	/**
	 * @return A Trail Manager for this solar system grid.
	 */
	public ShipTrailManager getTrailManager()
	{
		return aTrailManager;
	}
}

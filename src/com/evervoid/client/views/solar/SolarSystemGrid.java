package com.evervoid.client.views.solar;

import java.util.Set;

import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.graphics.Grid;
import com.evervoid.client.graphics.GridNode;
import com.evervoid.state.GridLocation;
import com.evervoid.state.Point;
import com.evervoid.state.SolarSystem;
import com.jme3.math.ColorRGBA;

/**
 * This class represents the grid displayed when in the solar system view.
 */
public class SolarSystemGrid extends Grid
{
	private final SolarSystem aSolarSystem;
	private final SolarSystemView aSolarSystemView;
	private final ColorRGBA aStarGlowColor;
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
		aStarGlowColor = GraphicsUtils.getColorRGBA(ss.getSunGlowColor());
	}

	@Override
	public void computeTransforms()
	{
		super.computeTransforms();
		aSolarSystemView.computeGridDimensions();
	}

	/**
	 * Return a UIProp at a certain location
	 * 
	 * @param point
	 *            The Point to look at
	 * @return The prop at the given point, or null if there is no such prop
	 */
	protected UIProp getPropAt(final Point point)
	{
		final Set<GridNode> nodes = getNodeList(point);
		// This looks a bit silly, but it works
		for (final GridNode n : nodes) {
			return (UIProp) n;
		}
		return null;
	}

	/**
	 * @return The glow color of the sun
	 */
	public ColorRGBA getSunGlowColor()
	{
		return aStarGlowColor;
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

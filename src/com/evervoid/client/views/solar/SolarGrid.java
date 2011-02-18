package com.evervoid.client.views.solar;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.graphics.Grid;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.prop.Prop;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * This class represents the grid displayed when in the solar system view.
 */
public class SolarGrid extends Grid
{
	public static final int sCellSize = 64;
	private final SolarGridSelection aGridHover;
	private final Map<Prop, UIProp> aProps = new HashMap<Prop, UIProp>();
	private final UIShip aSelectedShip = null;
	private final SolarSystem aSolarSystem;
	private final SolarView aSolarSystemView;
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
	public SolarGrid(final SolarView view, final SolarSystem ss)
	{
		super(ss.getDimension(), sCellSize, sCellSize, 1, new ColorRGBA(1f, 1f, 1f, 0.2f));
		aSolarSystemView = view;
		aSolarSystem = ss;
		aStarGlowColor = GraphicsUtils.getColorRGBA(ss.getSunShadowColor());
		aGridHover = new SolarGridSelection();
		addNode(aGridHover);
	}

	@Override
	public void computeTransforms()
	{
		super.computeTransforms();
		aSolarSystemView.computeGridDimensions();
	}

	AnimatedAlpha getLineAlphaAnimation()
	{
		return aLines.getNewAlphaAnimation();
	}

	/**
	 * Finds if there is a UIProp at the given point
	 * 
	 * @param position
	 *            The point to look at
	 * @return The UIProp at the given point, or null if there is no prop there
	 */
	UIProp getPropAt(final Point point)
	{
		final Prop found = aSolarSystem.getPropAt(point);
		if (found == null) {
			return null;
		}
		return aProps.get(found);
	}

	/**
	 * @return A GridLocation where the sun is located.
	 */
	public GridLocation getSunLocation()
	{
		return aSolarSystem.getSunLocation();
	}

	/**
	 * @return The glow color of the sun
	 */
	public ColorRGBA getSunShadowColor()
	{
		return aStarGlowColor;
	}

	/**
	 * @return A Trail Manager for this solar system grid.
	 */
	public ShipTrailManager getTrailManager()
	{
		return aTrailManager;
	}

	/**
	 * Handle hover events on the grid
	 * 
	 * @param position
	 *            Grid-based position that was hovered
	 * @return Whether the cursor was on the grid or not
	 */
	boolean hover(final Vector2f position)
	{
		final Point pointed = getCellAt(position);
		if (pointed == null) {
			aGridHover.fadeOut();
			return false;
		}
		else {
			aGridHover.fadeIn();
		}
		// Take care of selection square
		final UIProp prop = getPropAt(pointed);
		if (prop == null) {
			aGridHover.goTo(new GridLocation(pointed));
		}
		else {
			aGridHover.goTo(prop.getLocation());
		}
		// tmpShip.faceTowards(hoveredPoint);
		return true;
	}
}

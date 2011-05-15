package com.evervoid.state.prop;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.Wormhole;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;

public class Portal extends Prop
{
	/**
	 * The side to which the Portal is attached. This will determine orientation and where the Portal's exits are.
	 */
	public static enum GridEdge
	{
		BOTTOM, LEFT, RIGHT, TOP;
	}

	/**
	 * The dimensions of a horizontal Portal.
	 */
	public static final Dimension sHorizontal = new Dimension(4, 1);
	/**
	 * The dimensions of a vertical Portal.
	 */
	public static final Dimension sVertial = new Dimension(1, 4);
	/**
	 * The edge with which this Portal is associated.
	 */
	private final GridEdge aOrientation;
	/**
	 * The Wormhole this Portal links to.
	 */
	private final Wormhole aWormhole;

	/**
	 * Creates a Portal with the parameters passed.
	 * 
	 * @param id
	 *            The ID of the Portal in its state.
	 * @param location
	 *            The location of the Portal in its SolarSystem.
	 * @param local
	 *            The local SolarSystem in which the Portal resides.
	 * @param dest
	 *            The Wormhole this Portal links.
	 * @param state
	 *            The state to which this Portal belongs.
	 */
	public Portal(final int id, final GridLocation location, final SolarSystem local, final Wormhole dest,
			final EVGameState state)
	{
		super(id, state.getNullPlayer(), location, "portal", state);
		aContainer = local;
		aWormhole = dest;
		if (location.getY() == local.getHeight() - 1 && location.getHeight() == 1) {
			aOrientation = GridEdge.TOP;
		}
		else if (location.getX() == local.getWidth() - 1 && location.getWidth() == 1) {
			aOrientation = GridEdge.RIGHT;
		}
		else if (location.getY() == 0 && location.getHeight() == 1) {
			aOrientation = GridEdge.BOTTOM;
		}
		else {
			aOrientation = GridEdge.LEFT;
		}
	}

	/**
	 * Creates a Portal from the contents of the Json, getting parameters from the state.
	 * 
	 * @param j
	 *            The Json containing the Portal's information.
	 * @param state
	 *            The state to wihch this Portal will belong.
	 */
	public Portal(final Json j, final EVGameState state)
	{
		super(j, state);
		aWormhole = state.getGalaxy().getWormhole(j.getIntAttribute("destination"));
		aOrientation = GridEdge.values()[j.getIntAttribute("orientation")];
		aState.registerProp(this, aContainer);
	}

	/**
	 * @return Whether the parameter SolarSystem is this Portal's destination.
	 */
	public boolean connects(final SolarSystem ss)
	{
		return aWormhole.connects(getContainer(), ss);
	}

	@Override
	public SolarSystem getContainer()
	{
		return (SolarSystem) aContainer;
	}

	/**
	 * @return The Portal linked to this one by their Wormhole.
	 */
	public Portal getDestinationPortal()
	{
		if (aWormhole.getPortal1().equals(this)) {
			return aWormhole.getPortal2();
		}
		else {
			return aWormhole.getPortal1();
		}
	}

	/**
	 * @return The edge to wich this Portal belongs in its container SolarSystem.
	 */
	public GridEdge getGridEdge()
	{
		return aOrientation;
	}

	/**
	 * @return The set of points from which a Ship of Dimmension dim could enter this Portal.
	 */
	public Set<Point> getJumpingLocations(final Dimension dim)
	{
		final Set<Point> points = new HashSet<Point>();
		final Point jumpingPoint = getLocation().origin;
		switch (aOrientation) {
			case TOP:
			case BOTTOM:
				for (int x = 0; x <= getWidth() - dim.width; x++) {
					points.add(jumpingPoint.add(x, aOrientation.equals(GridEdge.TOP) ? -dim.height : getHeight()));
				}
				break;
			case RIGHT:
			case LEFT:
				for (int y = 0; y <= getHeight() - dim.height; y++) {
					points.add(jumpingPoint.add(aOrientation.equals(GridEdge.RIGHT) ? -dim.width : getWidth(), y));
				}
		}
		return points;
	}

	/**
	 * @return The SolarSystem to which this Portal belongs.
	 */
	public SolarSystem getSolarSystem()
	{
		return getContainer();
	}

	/**
	 * @return The Wormhole this Portal is attached to.
	 */
	public Wormhole getWormhole()
	{
		return aWormhole;
	}

	/**
	 * @return Whether this Portal is in a Horizontal position.
	 */
	public boolean isHorizontal()
	{
		return aOrientation.equals(GridEdge.TOP) || aOrientation.equals(GridEdge.BOTTOM);
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("destination", aWormhole.getID());
		j.setAttribute("orientation", aOrientation.ordinal());
		return j;
	}
}

package com.evervoid.state.prop;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.Galaxy;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.player.Player;

public class Portal extends Prop
{
	public static enum GridEdge
	{
		BOTTOM, LEFT, RIGHT, TOP;
	}

	private final SolarSystem aDestinationSS;
	private final GridEdge aOrientation;

	public Portal(final int id, final Player player, final GridLocation location, final SolarSystem local,
			final SolarSystem dest)
	{
		super(id, player, location, "portal");
		aContainer = local;
		aDestinationSS = dest;
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

	public Portal(final Json j, final EVGameState state, final Galaxy galaxy)
	{
		super(j, state.getNullPlayer(), "portal", state);
		aDestinationSS = galaxy.getSolarSystem(j.getIntAttribute("destination"));
		aOrientation = GridEdge.values()[j.getIntAttribute("orientation")];
	}

	@Override
	public SolarSystem getContainer()
	{
		return (SolarSystem) aContainer;
	}

	public SolarSystem getDestination()
	{
		return aDestinationSS;
	}

	public Point getJumpingLocation(final Dimension dim)
	{
		final Point jumpingPoint = getLocation().origin;
		switch (aOrientation) {
			case TOP:
				return jumpingPoint.add(getWidth() / 2 - dim.width / 2, -dim.height);
			case RIGHT:
				return jumpingPoint.add(-dim.width, getHeight() / 2 - dim.height / 2);
			case BOTTOM:
				return jumpingPoint.add(getWidth() / 2 - dim.width / 2, getHeight());
			case LEFT:
				return jumpingPoint.add(getWidth(), getHeight() / 2 - dim.height / 2);
		}
		return null;
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("destination", aDestinationSS.getID());
		j.setIntAttribute("orientation", aOrientation.ordinal());
		return j;
	}
}

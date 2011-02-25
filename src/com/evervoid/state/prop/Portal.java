package com.evervoid.state.prop;

import java.util.HashSet;
import java.util.Set;

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

	public Set<Point> getJumpingLocations(final Dimension dim)
	{
		final Set<Point> points = new HashSet<Point>();
		final Point jumpingPoint = getLocation().origin;
		switch (aOrientation) {
			case TOP:
			case BOTTOM:
				for (int x = 0; x < getWidth() - dim.width; x++) {
					points.add(jumpingPoint.add(x, aOrientation.equals(GridEdge.TOP) ? -dim.height : getHeight()));
				}
				break;
			case RIGHT:
			case LEFT:
				for (int y = 0; y < getHeight() - dim.height; y++) {
					points.add(jumpingPoint.add(aOrientation.equals(GridEdge.RIGHT) ? -dim.width : getWidth(), y));
				}
		}
		return points;
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

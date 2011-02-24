package com.evervoid.state.prop;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.player.Player;

public class Portal extends Prop
{
	private final SolarSystem aDestinationSS;

	public Portal(final int id, final Player player, final GridLocation location, final SolarSystem local,
			final SolarSystem dest)
	{
		super(id, player, location, "portal");
		aContainer = local;
		aDestinationSS = dest;
	}

	public Portal(final Json j, final EVGameState state)
	{
		super(j, state.getNullPlayer(), "portal", state);
		aDestinationSS = state.getSolarSystem(j.getIntAttribute("dest"));
		enterContainer(aContainer);
	}

	@Override
	public SolarSystem getContainer()
	{
		return (SolarSystem) aContainer;
	}

	public int getHeight()
	{
		return aLocation.getHeight();
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("dest", aDestinationSS.getID());
		return j;
	}
}

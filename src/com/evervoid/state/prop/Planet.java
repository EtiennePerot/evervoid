package com.evervoid.state.prop;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.data.PlanetData;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.player.Player;

public class Planet extends Prop
{
	private final PlanetData aData;

	public Planet(final Json j, final EVGameState state)
	{
		super(j, state, "planet");
		aData = state.getPlanetData(j.getStringAttribute("planettype"));
	}

	public Planet(final Player player, final GridLocation location, final String type, final EVGameState state)
	{
		super(player, location, state, "planet");
		aData = state.getPlanetData(type);
		aLocation.dimension = aData.getDimension();
	}

	public PlanetData getData()
	{
		return aData;
	}

	@Override
	public Json toJson()
	{
		return basePropJson().setStringAttribute("planettype", aData.getType());
	}
}

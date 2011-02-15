package com.evervoid.state.prop;

import com.evervoid.gamedata.PlanetData;
import com.evervoid.json.Json;
import com.evervoid.state.EverVoidGameState;
import com.evervoid.state.GridLocation;
import com.evervoid.state.player.Player;

public class Planet extends Prop
{
	private final PlanetData aData;

	public Planet(final Json j, final EverVoidGameState state)
	{
		super(j, state);
		aData = state.getPlanetData(j.getStringAttribute("planettype"));
	}

	public Planet(final Player player, final GridLocation location, final String type, final EverVoidGameState state)
	{
		super(player, location, state);
		aData = state.getPlanetData(type);
		aLocation.dimension = aData.getDimension();
	}

	public PlanetData getData()
	{
		return aData;
	}

	@Override
	public String getPropType()
	{
		return "planet";
	}

	@Override
	public Json toJson()
	{
		return super.toJson().setStringAttribute("planettype", aData.getType());
	}
}

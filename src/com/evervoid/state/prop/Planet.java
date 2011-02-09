package com.evervoid.state.prop;

import com.evervoid.gamedata.PlanetData;
import com.evervoid.json.Json;
import com.evervoid.state.EverVoidGameState;
import com.evervoid.state.GridLocation;
import com.evervoid.state.player.Player;

public class Planet extends Prop
{
	public static Planet fromJson(final Json j, final EverVoidGameState state)
	{
		return new Planet(state.getPlayerByName(j.getStringAttribute("player")), GridLocation.fromJson(j
				.getAttribute("location")), state.getPlanetData(j.getStringAttribute("type")));
	}

	private final PlanetData aData;

	public Planet(final Player player, final GridLocation location, final PlanetData data)
	{
		super(player, location);
		aData = data;
		aLocation.dimension = data.getDimension();
	}

	public PlanetData getData()
	{
		return aData;
	}

	@Override
	public Json toJson()
	{
		return super.toJson().setStringAttribute("proptype", "planet").setStringAttribute("type", aData.getType());
	}
}

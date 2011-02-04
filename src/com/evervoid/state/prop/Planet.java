package com.evervoid.state.prop;

import com.evervoid.gamedata.PlanetData;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.GridLocation;
import com.evervoid.state.player.Player;

public class Planet extends Prop implements Jsonable
{
	public static Planet fromJson(final Json j, final Player player)
	{
		return new Planet(player, GridLocation.fromJson(j.getAttribute("location")), j.getStringAttribute("type"));
	}

	private final PlanetData aData;

	public Planet(final Player player, final GridLocation location, final PlanetData data)
	{
		super(player, location);
		aData = data;
		aLocation.dimension = data.getDimension();
	}

	public Planet(final Player player, final GridLocation location, final String data)
	{
		this(player, location, new PlanetData(data));
	}

	public PlanetData getData()
	{
		return aData;
	}

	@Override
	public Json toJson()
	{
		return new Json().setStringAttribute("type", aData.getType()).setAttribute("location", aLocation);
	}
}

package com.evervoid.state.prop;

import com.evervoid.gamedata.PlanetData;
import com.evervoid.state.GridLocation;
import com.evervoid.state.player.Player;

public class Planet extends Prop
{
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
}

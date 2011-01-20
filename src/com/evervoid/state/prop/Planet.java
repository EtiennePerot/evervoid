package com.evervoid.state.prop;

import com.evervoid.gamedata.PlanetData;
import com.evervoid.state.Point;
import com.evervoid.state.player.Player;

public class Planet extends Prop
{
	private final PlanetData aData;

	public Planet(final Player player, final Point point, final PlanetData data)
	{
		super(player, point);
		aData = data;
	}

	public Planet(final Player player, final Point point, final String data)
	{
		this(player, point, new PlanetData(data));
	}

	public PlanetData getData()
	{
		return aData;
	}
}

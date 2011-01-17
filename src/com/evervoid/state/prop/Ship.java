package com.evervoid.state.prop;

import com.evervoid.gamedata.ShipData;
import com.evervoid.state.player.Player;
import com.evervoid.state.solar.Point;
import com.jme3.math.ColorRGBA;

public class Ship extends Prop
{
	private final ShipData aData;

	public Ship(final Player player, final Point point, final ShipData data)
	{
		super(player, point);
		aData = data;
	}

	public Ship(final Player player, final Point point, final String data)
	{
		this(player, point, new ShipData(data));
	}

	public ColorRGBA getColor()
	{
		// TODO: Get actual color from player object
		return ColorRGBA.randomColor();
	}

	public ShipData getData()
	{
		return aData;
	}
}

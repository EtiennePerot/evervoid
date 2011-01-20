package com.evervoid.state.prop;

import com.evervoid.gamedata.Dimension;
import com.evervoid.gamedata.OffsetSprite;
import com.evervoid.gamedata.ShipData;
import com.evervoid.state.Point;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.PlayerColor;

public class Ship extends Prop
{
	private final ShipData aData;

	public Ship(final Player player, final Point point, final String data)
	{
		super(player, point);
		aData = ShipData.getShipData(data);
	}

	public OffsetSprite getBaseSprite()
	{
		return aData.getBaseSprite();
	}

	public PlayerColor getColor()
	{
		return aPlayer.getColor();
	}

	public OffsetSprite getColorOverlay()
	{
		return aData.getColorOverlay();
	}

	@Override
	public Dimension getDimension()
	{
		return aData.getDimension();
	}

	public float getMovingTime()
	{
		return aData.getMovingTime();
	}

	public float getRotationSpeed()
	{
		return aData.getRotationSpeed();
	}
}

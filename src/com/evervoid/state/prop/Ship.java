package com.evervoid.state.prop;

import com.evervoid.gamedata.OffsetSprite;
import com.evervoid.gamedata.ShipData;
import com.evervoid.state.GridLocation;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.PlayerColor;

public class Ship extends Prop
{
	private final ShipData aData;

	public Ship(final Player player, final GridLocation location, final String data)
	{
		super(player, location);
		aData = ShipData.getShipData(data);
		// Overwrite GridLocation dimension with data from ship data
		aLocation.dimension = aData.getDimension();
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

	public float getMovingTime()
	{
		return aData.getMovingTime();
	}

	public float getRotationSpeed()
	{
		return aData.getRotationSpeed();
	}
}

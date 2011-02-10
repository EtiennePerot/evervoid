package com.evervoid.state.prop;

import com.evervoid.gamedata.ShipData;
import com.evervoid.gamedata.TrailData;
import com.evervoid.json.Json;
import com.evervoid.state.GridLocation;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.PlayerColor;

public class Ship extends Prop
{
	private final ShipData aData;

	public Ship(final Player player, final GridLocation location, final String shipType)
	{
		super(player, location);
		aData = aPlayer.getRaceData().getShipData(shipType);
		// Overwrite GridLocation dimension with data from ship data
		aLocation.dimension = aData.getDimension();
	}

	public PlayerColor getColor()
	{
		return aPlayer.getColor();
	}

	public ShipData getData()
	{
		return aData;
	}

	public TrailData getTrailData()
	{
		// TODO: Make this depend on research
		// FIXME: Haaaax
		return aPlayer.getRaceData().getTrailData("engine_0");
	}

	@Override
	public Json toJson()
	{
		return super.toJson().setStringAttribute("proptype", "ship").setStringAttribute("type", aData.getType());
	}
}

package com.evervoid.state.prop;

import com.evervoid.gamedata.ShipData;
import com.evervoid.gamedata.TrailData;
import com.evervoid.json.Json;
import com.evervoid.state.Color;
import com.evervoid.state.EverVoidGameState;
import com.evervoid.state.GridLocation;
import com.evervoid.state.player.Player;

public class Ship extends Prop
{
	private final ShipData aData;

	public Ship(final Json j, final EverVoidGameState state)
	{
		super(j, state, "ship");
		aData = aPlayer.getRaceData().getShipData(j.getStringAttribute("shiptype"));
		// Overwrite GridLocation dimension with data from ship data
		aLocation.dimension = aData.getDimension();
	}

	public Ship(final Player player, final GridLocation location, final String shipType, final EverVoidGameState state)
	{
		super(player, location, state, "ship");
		aData = aPlayer.getRaceData().getShipData(shipType);
		// Overwrite GridLocation dimension with data from ship data
		aLocation.dimension = aData.getDimension();
	}

	public Color getColor()
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
		return super.toJson().setStringAttribute("shiptype", aData.getType());
	}
}

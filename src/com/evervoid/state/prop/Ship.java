package com.evervoid.state.prop;

import com.evervoid.json.Json;
import com.evervoid.state.Color;
import com.evervoid.state.EVGameState;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.data.TrailData;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.player.Player;

public class Ship extends Prop
{
	private final ShipData aData;

	public Ship(final Json j, final EVGameState state)
	{
		super(j, state, "ship");
		aData = aPlayer.getRaceData().getShipData(j.getStringAttribute("shiptype"));
		// Overwrite GridLocation dimension with data from ship data
		aLocation.dimension = aData.getDimension();
	}

	public Ship(final Player player, final GridLocation location, final String shipType, final EVGameState state)
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
		return basePropJson().setStringAttribute("shiptype", aData.getType());
	}
}

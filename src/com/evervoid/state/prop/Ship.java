package com.evervoid.state.prop;

import com.evervoid.gamedata.ShipData;
import com.evervoid.json.Json;
import com.evervoid.state.EverVoidGameState;
import com.evervoid.state.GridLocation;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.PlayerColor;

public class Ship extends Prop
{
	public static Ship fromJson(final Json j, final EverVoidGameState state)
	{
		return new Ship(state.getPlayerByName(j.getStringAttribute("player")),
				GridLocation.fromJson(j.getAttribute("location")), state.getShipData(j.getStringAttribute("type")));
	}

	private final ShipData aData;

	public Ship(final Player player, final GridLocation location, final ShipData data)
	{
		super(player, location);
		aData = data;
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

	public TrailInfo getTrailInfo()
	{
		return TrailInfo.getRaceTrail(aPlayer.getRace(), aPlayer.getResearch());
	}

	@Override
	public Json toJson()
	{
		return super.toJson().setStringAttribute("type", aData.getType());
	}
}

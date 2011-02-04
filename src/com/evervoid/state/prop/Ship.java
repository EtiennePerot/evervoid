package com.evervoid.state.prop;

import com.evervoid.gamedata.ShipData;
import com.evervoid.json.Json;
import com.evervoid.state.GridLocation;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.PlayerColor;

public class Ship extends Prop
{
	public static Ship fromJson(final Json j, final Player player)
	{
		return new Ship(player, GridLocation.fromJson(j.getAttribute("location")), j.getStringAttribute("type"));
	}

	private final ShipData aData;

	public Ship(final Player player, final GridLocation location, final String data)
	{
		super(player, location);
		aData = ShipData.getShipData(data);
		// Overwrite GridLocation dimension with data from ship data
		aLocation.dimension = aData.getDimension();
		System.out.println(toJson().toPrettyString());
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
		return new Json().setStringAttribute("type", aData.getType()).setAttribute("location", aLocation);
	}
}

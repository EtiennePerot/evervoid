package com.evervoid.gamedata;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class RaceData implements Jsonable
{
	private final Map<String, ShipData> aShipData = new HashMap<String, ShipData>();
	private final Map<String, TrailData> aTrailData = new HashMap<String, TrailData>();
	private final String aType;

	RaceData(final String race, final Json j)
	{
		aType = race;
		final Json shipJson = j.getAttribute("ships");
		for (final String ship : shipJson.getAttributes()) {
			aShipData.put(ship, new ShipData(ship, shipJson.getAttribute(ship)));
		}
		final Json trailJson = j.getAttribute("trails");
		for (final String trail : trailJson.getAttributes()) {
			aTrailData.put(trail, new TrailData(trail, trailJson.getAttribute(trail)));
		}
	}

	public ShipData getShipData(final String shipType)
	{
		return aShipData.get(shipType);
	}

	public TrailData getTrailData(final String trailType)
	{
		return aTrailData.get(trailType);
	}

	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		return new Json().setMapAttribute("ships", aShipData).setMapAttribute("trails", aTrailData);
	}
}

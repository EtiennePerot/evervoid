package com.evervoid.state.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class RaceData implements Jsonable
{
	private final Map<String, ResearchTree> aResearchTrees = new HashMap<String, ResearchTree>();
	private final Map<String, ShipData> aShipData = new HashMap<String, ShipData>();
	private final Map<String, TrailData> aTrailData = new HashMap<String, TrailData>();
	private final String aType;

	RaceData(final String race, final Json j)
	{
		aType = race;
		final Json shipJson = j.getAttribute("ships");
		for (final String ship : shipJson.getAttributes()) {
			aShipData.put(ship, new ShipData(ship, race, shipJson.getAttribute(ship)));
		}
		final Json trailJson = j.getAttribute("trails");
		for (final String trail : trailJson.getAttributes()) {
			aTrailData.put(trail, new TrailData(trail, race, trailJson.getAttribute(trail)));
		}
		final Json researchJson = j.getAttribute("research");
		for (final String research : researchJson.getAttributes()) {
			aResearchTrees.put(research, new ResearchTree(research, race, researchJson.getAttribute(research)));
		}
	}

	public ResearchTree getResearchTree(final String researchTree)
	{
		return aResearchTrees.get(researchTree);
	}

	public ShipData getShipData(final String shipType)
	{
		return aShipData.get(shipType);
	}

	public Set<String> getShipTypes()
	{
		return aShipData.keySet();
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
		return new Json().setMapAttribute("ships", aShipData).setMapAttribute("trails", aTrailData)
				.setMapAttribute("research", aResearchTrees);
	}
}

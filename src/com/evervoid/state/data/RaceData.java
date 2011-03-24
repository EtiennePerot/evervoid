package com.evervoid.state.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.player.ResourceAmount;

public class RaceData implements Jsonable
{
	private final ResourceAmount aInitialResources;
	private final Map<String, ResearchTree> aResearchTrees = new HashMap<String, ResearchTree>();
	private final Map<String, ShipData> aShipData = new HashMap<String, ShipData>();
	private final String aTitle;
	private final Map<String, TrailData> aTrailData = new HashMap<String, TrailData>();
	private final String aType;

	RaceData(final String race, final Json j)
	{
		aType = race;
		aTitle = j.getStringAttribute("title");
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
		aInitialResources = new ResourceAmount(j.getAttribute("initialResources"));
	}

	public String getRaceIcon(final String style)
	{
		return "icons/races/" + aType + "/" + style + ".png";
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

	public ResourceAmount getStartResources()
	{
		return aInitialResources;
	}

	public String getTitle()
	{
		return aTitle;
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
				.setMapAttribute("research", aResearchTrees).setStringAttribute("title", aTitle)
				.setAttribute("initialResources", aInitialResources);
	}
}

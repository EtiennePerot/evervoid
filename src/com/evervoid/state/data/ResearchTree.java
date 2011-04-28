package com.evervoid.state.data;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

/**
 * Represents a single research tree
 */
public class ResearchTree implements Jsonable
{
	private final Map<String, ResearchData> aResearches = new HashMap<String, ResearchData>();
	private final String aTitle;
	private final String aType;

	public ResearchTree(final String researchTree, final String race, final Json j)
	{
		aType = researchTree;
		aTitle = j.getStringAttribute("title");
		final Json upgrades = j.getAttribute("upgrades");
		for (final String research : upgrades.getAttributes()) {
			aResearches.put(research, new ResearchData(research, researchTree, race, upgrades.getAttribute(research)));
		}
	}

	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("title", aTitle).setMapAttribute("upgrades", aResearches);
	}
}

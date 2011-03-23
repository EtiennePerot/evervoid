package com.evervoid.state.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.data.GameData;
import com.evervoid.state.data.PlanetData;
import com.evervoid.state.data.RaceData;

public class ResourceAmount implements Jsonable
{
	private final Map<String, Integer> aResourceMap;

	public ResourceAmount(final GameData data, final RaceData race)
	{
		aResourceMap = new HashMap<String, Integer>();
		for (final String resource : data.getResources()) {
			aResourceMap.put(resource, race.getStartValue(resource));
		}
	}

	public ResourceAmount(final Json j)
	{
		aResourceMap = new HashMap<String, Integer>();
		final Json resourceJson = j.getAttribute("resources");
		for (final String resource : resourceJson.getAttributes()) {
			aResourceMap.put(resource, resourceJson.getIntAttribute(resource));
		}
	}

	public ResourceAmount(final PlanetData data)
	{
		aResourceMap = new HashMap<String, Integer>();
	}

	public boolean add(final String resource, final int amount)
	{
		if (!aResourceMap.containsKey(resource)) {
			return false;
		}
		aResourceMap.put(resource, Math.max(0, aResourceMap.get(resource) + amount));
		return true;
	}

	public Set<String> getNames()
	{
		return aResourceMap.keySet();
	}

	public int getValue(final String resourceName)
	{
		return aResourceMap.get(resourceName);
	}

	public boolean remove(final String resource, final int amount)
	{
		return add(resource, -amount);
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		final Json map = new Json();
		for (final String resource : aResourceMap.keySet()) {
			map.setIntAttribute(resource, aResourceMap.get(resource));
		}
		j.setAttribute("resources", map);
		return j;
	}
}
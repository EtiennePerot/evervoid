package com.evervoid.state.data;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.geometry.Dimension;

public class PlanetData implements Jsonable
{
	private final SpriteData aBaseSprite;
	private final Dimension aDimension;
	private final String aPlanetType;
	private final Map<String, Integer> aResourceRates;
	private final String aType;

	PlanetData(final String type, final Json j)
	{
		aType = type;
		aPlanetType = j.getStringAttribute("planettype");
		aDimension = new Dimension(j.getAttribute("dimension"));
		aBaseSprite = new SpriteData("planets/" + aPlanetType + "/" + aType + ".png");
		aResourceRates = new HashMap<String, Integer>();
		final Json resourceJson = j.getAttribute("resources");
		for (final String resource : resourceJson.getAttributes()) {
			aResourceRates.put(resource, resourceJson.getIntAttribute(resource));
		}
	}

	public SpriteData getBaseSprite()
	{
		return aBaseSprite;
	}

	public Dimension getDimension()
	{
		return aDimension;
	}

	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setAttribute("dimension", aDimension).setStringAttribute("planettype", aPlanetType);
		j.setMappedIntAttribute("resources", aResourceRates);
		return j;
	}
}

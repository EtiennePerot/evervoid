package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.player.ResourceAmount;

public class PlanetData implements Jsonable
{
	private final SpriteData aBaseSprite;
	private final Dimension aDimension;
	private final String aPlanetType;
	private final ResourceAmount aResourceRates;
	private final String aType;

	PlanetData(final String type, final Json j)
	{
		aType = type;
		aPlanetType = j.getStringAttribute("planettype");
		aDimension = new Dimension(j.getAttribute("dimension"));
		aBaseSprite = new SpriteData("planets/" + aPlanetType + "/" + aType + ".png");
		aResourceRates = new ResourceAmount(j.getAttribute("resources"));
	}

	public SpriteData getBaseSprite()
	{
		return aBaseSprite;
	}

	public Dimension getDimension()
	{
		return aDimension;
	}

	public ResourceAmount getResourceRate()
	{
		return aResourceRates;
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
		j.setAttribute("resources", aResourceRates);
		return j;
	}
}

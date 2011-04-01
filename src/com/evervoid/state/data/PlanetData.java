package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.player.ResourceAmount;

public class PlanetData implements Jsonable
{
	private final int aBaseHealth;
	private final SpriteData aBaseSprite;
	private final int aBuildingSlots;
	private final Dimension aDimension;
	private final String aPlanetType;
	private final ResourceAmount aResourceRates;
	private final String aTitle;
	private final String aType;

	PlanetData(final String type, final Json j)
	{
		aType = type;
		aPlanetType = j.getStringAttribute("planettype");
		aTitle = j.getStringAttribute("title");
		aDimension = new Dimension(j.getAttribute("dimension"));
		aBaseSprite = new SpriteData("planets/" + aPlanetType + "/" + aType + ".png");
		aResourceRates = new ResourceAmount(j.getAttribute("resources"));
		aBuildingSlots = j.getIntAttribute("slots");
		aBaseHealth = j.getIntAttribute("health");
	}

	public int getBaseHealth()
	{
		return aBaseHealth;
	}

	public SpriteData getBaseSprite()
	{
		return aBaseSprite;
	}

	public Dimension getDimension()
	{
		return aDimension;
	}

	public int getNumOfBuildingSlots()
	{
		return aBuildingSlots;
	}

	public ResourceAmount getResourceRate()
	{
		return aResourceRates;
	}

	public String getTitle()
	{
		return aTitle;
	}

	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setAttribute("dimension", aDimension);
		j.setStringAttribute("planettype", aPlanetType);
		j.setStringAttribute("title", aTitle);
		j.setAttribute("resources", aResourceRates);
		j.setIntAttribute("slots", aBuildingSlots);
		j.setIntAttribute("health", aBaseHealth);
		return j;
	}
}

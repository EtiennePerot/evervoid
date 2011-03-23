package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.geometry.Dimension;

public class PlanetData implements Jsonable
{
	private final SpriteData aBaseSprite;
	private final Dimension aDimension;
	private float aGasIncome = 0f;
	private final String aPlanetType;
	private final String aType;

	PlanetData(final String type, final Json j)
	{
		aType = type;
		aPlanetType = j.getStringAttribute("planettype");
		aDimension = new Dimension(j.getAttribute("dimension"));
		aBaseSprite = new SpriteData("planets/" + aPlanetType + "/" + aType + ".png");
		if (j.hasAttribute("gasincome")) {
			aGasIncome = j.getFloatAttribute("gasincome");
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

	public float getGasIncome()
	{
		return aGasIncome;
	}

	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json().setAttribute("dimension", aDimension).setStringAttribute("planettype", aPlanetType);
		if (aGasIncome != 0) {
			j.setFloatAttribute("gasincome", aGasIncome);
		}
		return j;
	}
}

package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Color;
import com.evervoid.state.geometry.Dimension;

public class StarData implements Jsonable
{
	private final SpriteData aBorderSprite;
	private final Dimension aDimension;
	private final Color aGlowColor;
	private final float aRadiation;
	private final Color aShadowColor;
	private final SpriteData aSprite;
	private final String aType;

	public StarData(final String type, final Json j)
	{
		aType = type;
		aDimension = new Dimension(j.getAttribute("dimension"));
		aGlowColor = new Color(j.getAttribute("glow"));
		aShadowColor = new Color(j.getAttribute("shadow"));
		aRadiation = j.getFloatAttribute("radiation");
		aSprite = new SpriteData("stars/" + type + ".png");
		aBorderSprite = new SpriteData("stars/" + type + "_border.png");
	}

	public SpriteData getBorderSprite()
	{
		return aBorderSprite;
	}

	public Dimension getDimension()
	{
		return aDimension;
	}

	public Color getGlowColor()
	{
		return aGlowColor;
	}

	public float getRadiation()
	{
		return aRadiation;
	}

	public Color getShadowColor()
	{
		return aShadowColor;
	}

	public SpriteData getSprite()
	{
		return aSprite;
	}

	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setAttribute("radiation", aRadiation);
		j.setAttribute("glow", aGlowColor);
		j.setAttribute("shadow", aShadowColor);
		j.setAttribute("dimension", aDimension);
		return j;
	}
}

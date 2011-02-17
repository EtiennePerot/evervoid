package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Color;
import com.evervoid.state.Dimension;

public class StarData implements Jsonable
{
	private final SpriteData aBorderSprite;
	private final Dimension aDimension;
	private final Color aGlowColor;
	private final float aRadiation;
	private final SpriteData aSprite;
	private final String aType;

	public StarData(final String type, final Json j)
	{
		aType = type;
		aDimension = Dimension.fromJson(j.getAttribute("dimension"));
		aGlowColor = Color.fromJson(j.getAttribute("glow"));
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
		return new Json().setFloatAttribute("radiation", aRadiation).setAttribute("glow", aGlowColor)
				.setAttribute("dimension", aDimension);
	}
}

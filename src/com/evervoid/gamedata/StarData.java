package com.evervoid.gamedata;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Color;
import com.evervoid.state.Dimension;

public class StarData implements Jsonable
{
	private final Dimension aDimension;
	private final Color aGlowColor;
	private final float aRadiation;
	private final SpriteInfo aSprite;
	private final String aType;

	public StarData(final String type, final Json j)
	{
		aType = type;
		aDimension = Dimension.fromJson(j.getAttribute("dimension"));
		aGlowColor = Color.fromJson(j.getAttribute("glow"));
		aRadiation = j.getFloatAttribute("radiation");
		aSprite = new SpriteInfo("stars/" + type + ".png");
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

	public SpriteInfo getSprite()
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

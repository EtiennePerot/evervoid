package com.evervoid.gamedata;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Dimension;

public class PlanetData implements Jsonable
{
	private final SpriteInfo aBaseSprite;
	private final Dimension aDimension;
	private final String aType;

	PlanetData(final String type, final Json j)
	{
		aType = type;
		aDimension = Dimension.fromJson(j.getAttribute("dimension"));
		aBaseSprite = SpriteInfo.fromJson(j.getAttribute("basesprite"));
	}

	public SpriteInfo getBaseSprite()
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
		return new Json().setAttribute("basesprite", aBaseSprite).setAttribute("dimension", aDimension);
	}
}

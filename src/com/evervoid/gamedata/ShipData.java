package com.evervoid.gamedata;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Dimension;
import com.evervoid.state.Point;

public class ShipData implements Jsonable
{
	private final SpriteInfo aBaseColorOverlay;
	private final SpriteInfo aBaseSprite;
	private final Dimension aDimension;
	private final Point aEngineOffset;
	private final float aMovingTime;
	private final float aRotationSpeed;
	private final Point aTrailOffset;
	private final String aType;

	ShipData(final String shipType, final Json j)
	{
		aType = shipType;
		aBaseColorOverlay = SpriteInfo.fromJson(j.getAttribute("basecoloroverlay"));
		aBaseSprite = SpriteInfo.fromJson(j.getAttribute("basesprite"));
		aDimension = Dimension.fromJson(j.getAttribute("dimension"));
		aEngineOffset = Point.fromJson(j.getAttribute("engineoffset"));
		aMovingTime = j.getFloatAttribute("movingtime");
		aRotationSpeed = j.getFloatAttribute("rotationspeed");
		aTrailOffset = Point.fromJson(j.getAttribute("trailoffset"));
	}

	public SpriteInfo getBaseSprite()
	{
		return aBaseSprite;
	}

	public SpriteInfo getColorOverlay()
	{
		return aBaseColorOverlay;
	}

	public Dimension getDimension()
	{
		return aDimension;
	}

	public Point getEngineOffset()
	{
		return aEngineOffset;
	}

	public float getMovingTime()
	{
		return aMovingTime;
	}

	public float getRotationSpeed()
	{
		return aRotationSpeed;
	}

	public Point getTrailOffset()
	{
		return aTrailOffset;
	}

	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("basecoloroverlay", aBaseColorOverlay).setAttribute("basesprite", aBaseSprite)
				.setAttribute("dimension", aDimension).setAttribute("engineoffset", aEngineOffset)
				.setFloatAttribute("movingtime", aMovingTime).setFloatAttribute("rotationspeed", aRotationSpeed)
				.setAttribute("trailoffset", aTrailOffset);
	}
}

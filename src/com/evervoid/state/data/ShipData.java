package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.Point;

public class ShipData implements Jsonable
{
	private final SpriteData aBaseColorOverlay;
	private final SpriteData aBaseSprite;
	private final Dimension aDimension;
	private final Point aEngineOffset;
	private final float aMovingTime;
	private final float aRotationSpeed;
	private final int aSpeed;
	private final Point aTrailOffset;
	private final String aType;

	ShipData(final String shipType, final String race, final Json j)
	{
		aType = shipType;
		aBaseColorOverlay = new SpriteData("ships/" + race + "/" + shipType + "/color.png");
		aBaseSprite = new SpriteData("ships/" + race + "/" + shipType + "/base.png");
		aDimension = Dimension.fromJson(j.getAttribute("dimension"));
		aSpeed = j.getIntAttribute("speed");
		aEngineOffset = Point.fromJson(j.getAttribute("engineoffset"));
		aMovingTime = j.getFloatAttribute("movingtime");
		aRotationSpeed = j.getFloatAttribute("rotationspeed");
		aTrailOffset = Point.fromJson(j.getAttribute("trailoffset"));
	}

	public SpriteData getBaseSprite()
	{
		return aBaseSprite;
	}

	public SpriteData getColorOverlay()
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

	public int getSpeed()
	{
		return aSpeed;
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
		return new Json().setAttribute("dimension", aDimension).setIntAttribute("speed", aSpeed)
				.setAttribute("engineoffset", aEngineOffset).setFloatAttribute("movingtime", aMovingTime)
				.setFloatAttribute("rotationspeed", aRotationSpeed).setAttribute("trailoffset", aTrailOffset);
	}
}

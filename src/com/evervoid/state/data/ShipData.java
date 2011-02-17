package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Dimension;
import com.evervoid.state.geometry.Point;

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

	ShipData(final String shipType, final String race, final Json j)
	{
		aType = shipType;
		aBaseColorOverlay = new SpriteInfo("ships/" + race + "/" + shipType + "/color.png");
		aBaseSprite = new SpriteInfo("ships/" + race + "/" + shipType + "/base.png");
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
		return new Json().setAttribute("dimension", aDimension).setAttribute("engineoffset", aEngineOffset)
				.setFloatAttribute("movingtime", aMovingTime).setFloatAttribute("rotationspeed", aRotationSpeed)
				.setAttribute("trailoffset", aTrailOffset);
	}
}
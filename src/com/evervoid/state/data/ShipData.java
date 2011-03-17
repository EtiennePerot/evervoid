package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.Point;

public class ShipData implements Jsonable
{
	private final SpriteData aBaseColorOverlay;
	private final int aBaseHealth;
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
		aDimension = new Dimension(j.getAttribute("dimension"));
		aSpeed = j.getIntAttribute("speed");
		aEngineOffset = new Point(j.getAttribute("engineoffset"));
		aMovingTime = j.getFloatAttribute("movingtime");
		aRotationSpeed = j.getFloatAttribute("rotationspeed");
		aTrailOffset = new Point(j.getAttribute("trailoffset"));
		aBaseHealth = j.getIntAttribute("baseHealth");
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

	public int getMaximumHealth()
	{
		return aBaseHealth;
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
		final Json j = new Json();
		j.setAttribute("dimension", aDimension);
		j.setIntAttribute("speed", aSpeed);
		j.setAttribute("engineoffset", aEngineOffset);
		j.setFloatAttribute("movingtime", aMovingTime);
		j.setFloatAttribute("rotationspeed", aRotationSpeed);
		j.setAttribute("trailoffset", aTrailOffset);
		j.setIntAttribute("baseHealth", aBaseHealth);
		return j;
	}
}

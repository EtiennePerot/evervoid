package com.evervoid.state.data;

import java.util.HashMap;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.player.Research;

public class ShipData implements Jsonable
{
	private final SpriteData aBaseColorOverlay;
	private final int aBaseDamage;
	private final int aBaseHealth;
	private final SpriteData aBaseSprite;
	private final boolean aCanShoot;
	private final HashMap<String, Integer> aCost;
	private final Dimension aDimension;
	private final Point aEngineOffset;
	private final float aMovingTime;
	private final float aRotationSpeed;
	private final int aSpeed;
	private final String aTitle;
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
		aBaseHealth = j.getIntAttribute("basehealth");
		aBaseDamage = j.getIntAttribute("basedamage");
		aCanShoot = j.getBooleanAttribute("canshoot");
		aTitle = j.getStringAttribute("title");
		aCost = new HashMap<String, Integer>();
		final Json costs = j.getAttribute("cost");
		for (final String resource : costs.getAttributes()) {
			aCost.put(resource, costs.getIntAttribute(resource));
		}
	}

	public boolean canShoot()
	{
		return aCanShoot;
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

	public int getMaxDamage(final Research research)
	{
		// TODO - multiply by the reaserach damange offset
		return aBaseDamage;
	}

	public int getMaxHealth(final Research research)
	{
		// TODO - multiply by the reaserach damange offset
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

	public String getTitle()
	{
		return aTitle;
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
		j.setIntAttribute("basehealth", aBaseHealth);
		j.setIntAttribute("basedamage", aBaseDamage);
		j.setBooleanAttribute("canshoot", aCanShoot);
		j.setStringAttribute("title", aTitle);
		j.setMappedIntAttribute("cost", aCost);
		return j;
	}
}

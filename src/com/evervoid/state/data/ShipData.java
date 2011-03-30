package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.player.Research;
import com.evervoid.state.player.ResourceAmount;

public class ShipData implements Jsonable
{
	private final int aBaseBuildTime;
	private final SpriteData aBaseColorOverlay;
	private final ResourceAmount aBaseCost;
	private final int aBaseDamage;
	private final int aBaseHealth;
	private final int aBaseHealthRegen;
	private final int aBaseRadiation;
	private final int aBaseShieldRegen;
	private final int aBaseShields;
	private final int aBaseSpeed;
	private final SpriteData aBaseSprite;
	private final boolean aCanShoot;
	private final Dimension aDimension;
	private final Point aEngineOffset;
	private final float aMovingTime;
	private final float aRotationSpeed;
	private final String aTitle;
	private final Point aTrailOffset;
	private final String aType;

	ShipData(final String shipType, final String race, final Json j)
	{
		aType = shipType;
		aBaseColorOverlay = new SpriteData("ships/" + race + "/" + shipType + "/color.png");
		aBaseSprite = new SpriteData("ships/" + race + "/" + shipType + "/base.png");
		aDimension = new Dimension(j.getAttribute("dimension"));
		aBaseSpeed = j.getIntAttribute("speed");
		aEngineOffset = new Point(j.getAttribute("engineoffset"));
		aMovingTime = j.getFloatAttribute("movingTime");
		aRotationSpeed = j.getFloatAttribute("rotationSpeed");
		aTrailOffset = new Point(j.getAttribute("trailOffset"));
		aBaseHealth = j.getIntAttribute("baseHealth");
		aBaseHealthRegen = j.getIntAttribute("healthRegen");
		aBaseShields = j.getIntAttribute("baseShields");
		aBaseShieldRegen = j.getIntAttribute("shieldRegen");
		aBaseDamage = j.getIntAttribute("baseDamage");
		aCanShoot = j.getBooleanAttribute("canShoot");
		aTitle = j.getStringAttribute("title");
		aBaseCost = new ResourceAmount(j.getAttribute("cost"));
		aBaseBuildTime = j.getIntAttribute("buildTime");
		aBaseRadiation = j.getIntAttribute("baseRadiation");
	}

	public boolean canShoot()
	{
		return aCanShoot;
	}

	public int getBaseBuildTime()
	{
		return aBaseBuildTime;
	}

	public ResourceAmount getBaseCost()
	{
		return aBaseCost;
	}

	public SpriteData getBaseSprite()
	{
		return aBaseSprite;
	}

	public SpriteData getColorOverlay()
	{
		return aBaseColorOverlay;
	}

	public int getDamage(final Research research)
	{
		// TODO - multiply by the damage offset
		return aBaseDamage;
	}

	public Dimension getDimension()
	{
		return aDimension;
	}

	public Point getEngineOffset()
	{
		return aEngineOffset;
	}

	public int getHealth(final Research research)
	{
		// TODO - multiply by the health offset
		return aBaseHealth;
	}

	public int getHealthRegenRate(final Research research)
	{
		// TODO - multiply based on research
		return aBaseHealthRegen;
	}

	public float getMovingTime()
	{
		return aMovingTime;
	}

	public int getRadiation(final Research research)
	{
		// TODO - multiply by offset
		return aBaseRadiation;
	}

	public float getRotationSpeed()
	{
		return aRotationSpeed;
	}

	public int getShieldRegenRate(final Research reserach)
	{
		// TODO - multiply based on research
		return aBaseShieldRegen;
	}

	public int getShields(final Research research)
	{
		// TODO - multiply based on shields offset
		return aBaseShields;
	}

	public int getSpeed(final Research research)
	{
		return aBaseSpeed;
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
		j.setIntAttribute("speed", aBaseSpeed);
		j.setAttribute("engineoffset", aEngineOffset);
		j.setFloatAttribute("movingtime", aMovingTime);
		j.setFloatAttribute("rotationspeed", aRotationSpeed);
		j.setAttribute("trailoffset", aTrailOffset);
		j.setIntAttribute("basehealth", aBaseHealth);
		j.setIntAttribute("basedamage", aBaseDamage);
		j.setBooleanAttribute("canshoot", aCanShoot);
		j.setStringAttribute("title", aTitle);
		j.setAttribute("cost", aBaseCost);
		j.setIntAttribute("buildTime", aBaseBuildTime);
		j.setIntAttribute("baseRadiation", aBaseRadiation);
		j.setIntAttribute("baseShields", aBaseShields);
		j.setIntAttribute("shieldRegen", aBaseShieldRegen);
		j.setIntAttribute("healthRegen", aBaseShieldRegen);
		return j;
	}
}

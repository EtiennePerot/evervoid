package com.evervoid.state.data;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.player.Research;
import com.evervoid.state.player.ResourceAmount;

public class ShipData implements Jsonable
{
	private final int aBaseBuildTime;
	private final int aBaseCargoCapacity;
	private final SpriteData aBaseColorOverlay;
	private final ResourceAmount aBaseCost;
	private final int aBaseDamage;
	private final int aBaseDockingSize;
	private final int aBaseHealth;
	private final int aBaseHealthRegen;
	private final int aBaseRadiation;
	private final int aBaseShieldRegen;
	private final int aBaseShields;
	private final int aBaseSpeed;
	private final SpriteData aBaseSprite;
	private final boolean aCanShoot;
	private final Dimension aDimension;
	private final List<Point> aEngineOffset = new ArrayList<Point>(1);
	private final SpriteData aIconSprite;
	private final float aMovingTime;
	private final RaceData aRace;
	private final float aRotationSpeed;
	private final float aShieldScale;
	private final String aTitle;
	private final List<Point> aTrailOffset = new ArrayList<Point>(1);
	private final String aType;
	private final List<Point> aWeaponSlots = new ArrayList<Point>(1);

	ShipData(final String shipType, final RaceData race, final Json j)
	{
		aType = shipType;
		aRace = race;
		aBaseColorOverlay = new SpriteData("ships/" + aRace.getType() + "/" + aType + "/color.png");
		aBaseSprite = new SpriteData("ships/" + aRace.getType() + "/" + aType + "/base.png");
		aIconSprite = new SpriteData("ships/" + aRace.getType() + "/" + aType + "/icon.png");
		aDimension = new Dimension(j.getAttribute("dimension"));
		aBaseSpeed = j.getIntAttribute("speed");
		aMovingTime = j.getFloatAttribute("movingTime");
		aRotationSpeed = j.getFloatAttribute("rotationSpeed");
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
		aBaseCargoCapacity = Math.max(0, j.getIntAttribute("cargoCapacity"));
		aBaseDockingSize = Math.max(0, j.getIntAttribute("dockingSize"));
		aShieldScale = Math.max(0, j.getFloatAttribute("shieldscale"));
		for (final Json engine : j.getListAttribute("engineoffset")) {
			aEngineOffset.add(new Point(engine));
		}
		for (final Json trail : j.getListAttribute("trailOffset")) {
			aTrailOffset.add(new Point(trail));
		}
		for (final Json weapon : j.getListAttribute("weapons")) {
			aWeaponSlots.add(new Point(weapon));
		}
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

	public int getCargoCapacity(final Research research)
	{
		// TODO deal with research
		return aBaseCargoCapacity;
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

	public int getDockingSize()
	{
		return aBaseDockingSize;
	}

	public List<Point> getEngineOffsets()
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

	public SpriteData getIconSprite()
	{
		return aIconSprite;
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
		// TODO - Take research into account
		return aBaseShields;
	}

	public float getShieldScale()
	{
		return aShieldScale;
	}

	public SpriteData getShieldSprite(final Research research)
	{
		return aRace.getShieldSprite(research, getDimension());
	}

	public int getSpeed(final Research research)
	{
		return aBaseSpeed;
	}

	public String getTitle()
	{
		return aTitle;
	}

	public List<Point> getTrailOffsets()
	{
		return aTrailOffset;
	}

	public String getType()
	{
		return aType;
	}

	public List<Point> getWeaponSlots()
	{
		return aWeaponSlots;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setAttribute("dimension", aDimension);
		j.setIntAttribute("speed", aBaseSpeed);
		j.setListAttribute("engineoffset", aEngineOffset);
		j.setFloatAttribute("movingtime", aMovingTime);
		j.setFloatAttribute("rotationspeed", aRotationSpeed);
		j.setListAttribute("trailoffset", aTrailOffset);
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
		j.setIntAttribute("cargoCapacity", aBaseCargoCapacity);
		j.setIntAttribute("dockingSize", aBaseDockingSize);
		j.setListAttribute("weapons", aWeaponSlots);
		j.setFloatAttribute("shieldscale", aShieldScale);
		return j;
	}
}

package com.evervoid.state.data;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.player.Research;
import com.evervoid.state.player.ResourceAmount;
import com.evervoid.state.prop.Ship;

/**
 * ShipData contains all data needed to construct a {@link Ship}.
 */
public class ShipData implements Jsonable
{
	/**
	 * The base number of turns it takes to build Ships of this type.
	 */
	private final int aBaseBuildTime;
	/**
	 * The cargo bay size of Ships of this type.
	 */
	private final int aBaseCargoCapacity;
	/**
	 * The Color overlay sprite; this is the part of the Ship on which the Player's Color gets projected.
	 */
	private final SpriteData aBaseColorOverlay;
	/**
	 * The starting cost for Ships of this type.
	 */
	private final ResourceAmount aBaseCost;
	/**
	 * The starting damage dealt by Ships of this type.
	 */
	private final int aBaseDamage;
	/**
	 * The number of slots Ships of this type occupy in a cargo bay.
	 */
	private final int aBaseDockingSize;
	/**
	 * The starting health for Ships of this type.
	 */
	private final int aBaseHealthCapacity;
	/**
	 * The starting health regeneration rate for Ships of this type.
	 */
	private final int aBaseHealthRegen;
	/**
	 * The starting radiation capacity for Ships of this type.
	 */
	private final int aBaseRadiationCapacity;
	/**
	 * The starting Shield capacity for Ships of this type.
	 */
	private final int aBaseShieldCapacity;
	/**
	 * The starting Shield regeneration rate for Ships of this type.
	 */
	private final int aBaseShieldRegen;
	/**
	 * The starting speed for Ships of this type.
	 */
	private final int aBaseSpeed;
	/**
	 * The starting Sprite for Ships of this type.
	 */
	private final SpriteData aBaseSprite;
	/**
	 * Whether Ships of this type can shoot.
	 */
	private final boolean aCanShoot;
	/**
	 * The dimension of Ships of this type.
	 */
	private final Dimension aDimension;
	/**
	 * The Point representing the offsets at which the engine sprites are placed.
	 */
	private final List<Point> aEngineOffset = new ArrayList<Point>(1);
	/**
	 * The Sprite for the icon of this ship type.
	 */
	private final SpriteData aIconSprite;
	/**
	 * The time in seconds it takes for Ships of this type to animate their moves.
	 */
	private final float aMovingTime;
	/**
	 * The race that can build this Ship type.
	 */
	private final RaceData aRace;
	/**
	 * The speed at which this Ship rotates.
	 */
	private final float aRotationSpeed;
	/**
	 * The scale to be applied to the shield Sprite.
	 */
	private final float aShieldScale;
	/**
	 * The Ship type's in game title.
	 */
	private final String aTitle;
	/**
	 * The Point representing the offsets at which the trail sprites are placed.
	 */
	final List<Point> aTrailOffset = new ArrayList<Point>(1);
	/**
	 * The Ship type.
	 */
	private final String aType;
	/**
	 * The Point representing the offsets at which the turret sprites are placed.
	 */
	private final List<Point> aWeaponSlots = new ArrayList<Point>(1);

	/**
	 * Constructs a ShipData from the contents of the Json.
	 * 
	 * @param shipType
	 *            The Ship type this ShipData represents.
	 * @param race
	 *            The race that can build this Ship type.
	 * @param j
	 *            The Json containing the Data necessary for the ShipData.
	 */
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
		aBaseHealthCapacity = j.getIntAttribute("baseHealth");
		aBaseHealthRegen = j.getIntAttribute("healthRegen");
		aBaseShieldCapacity = j.getIntAttribute("baseShields");
		aBaseShieldRegen = j.getIntAttribute("shieldRegen");
		aBaseDamage = j.getIntAttribute("baseDamage");
		aCanShoot = j.getBooleanAttribute("canShoot");
		aTitle = j.getStringAttribute("title");
		aBaseCost = new ResourceAmount(j.getAttribute("cost"));
		aBaseBuildTime = j.getIntAttribute("buildTime");
		aBaseRadiationCapacity = j.getIntAttribute("baseRadiation");
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

	/**
	 * @return Whether Ships of this type can shoot.
	 */
	public boolean canShoot()
	{
		return aCanShoot;
	}

	/**
	 * @return The starting build time for Ships of this type.
	 */
	public int getBaseBuildTime()
	{
		return aBaseBuildTime;
	}

	/**
	 * @return The starting cost for Ships of this type.
	 */
	public ResourceAmount getBaseCost()
	{
		return aBaseCost;
	}

	/**
	 * @return The base sprite for Ships of this type.
	 */
	public SpriteData getBaseSprite()
	{
		return aBaseSprite;
	}

	/**
	 * @param research
	 *            The research level at which to grab the cargo capacity for this ShipData.
	 * @return The cargo capacity for Ships of this type at the given research level.
	 */
	public int getCargoCapacity(final Research research)
	{
		// TODO deal with research
		return aBaseCargoCapacity;
	}

	/**
	 * @return The sprite on which the Player's color will be overlaid.
	 */
	public SpriteData getColorOverlay()
	{
		return aBaseColorOverlay;
	}

	/**
	 * @param research
	 *            The research level at which to grab the damage for this ShipData.
	 * @return The damage dealt by Ships of this type at the given research level.
	 */
	public int getDamage(final Research research)
	{
		// TODO - multiply by the damage offset
		return aBaseDamage;
	}

	/**
	 * @return The dimension of Ships of this type.
	 */
	public Dimension getDimension()
	{
		return aDimension;
	}

	/**
	 * @return The docking size for Ships of this type.
	 */
	public int getDockingSize()
	{
		return aBaseDockingSize;
	}

	/**
	 * @return he offsets at which the engine sprites are placed.
	 */
	public List<Point> getEngineOffsets()
	{
		return aEngineOffset;
	}

	/**
	 * @param research
	 *            The research level at which to grab the health regeneration rate for this ShipData.
	 * @return The health regeneration rate for Ships of this type at the given research level.
	 */
	public int getHealthRegenRate(final Research research)
	{
		// TODO - multiply based on research
		return aBaseHealthRegen;
	}

	/**
	 * @return The icon sprite for this Ship type.
	 */
	public SpriteData getIconSprite()
	{
		return aIconSprite;
	}

	/**
	 * @param research
	 *            The research level at which to grab the maximum health for this ShipData.
	 * @return The maximum health for Ships of this type at the given research level.
	 */
	public int getMaximumHealth(final Research research)
	{
		// TODO - multiply by the health offset
		return aBaseHealthCapacity;
	}

	/**
	 * @return The moving time for Ships of this type.
	 */
	public float getMovingTime()
	{
		return aMovingTime;
	}

	/**
	 * @param research
	 *            The research level at which to grab the radiation capacity for this ShipData.
	 * @return The radiation capacity for Ships of this type at the given research level.
	 */
	public int getRadiationCapacity(final Research research)
	{
		// TODO - multiply by offset
		return aBaseRadiationCapacity;
	}

	/**
	 * @return The rotation speed for ships of this type.
	 */
	public float getRotationSpeed()
	{
		return aRotationSpeed;
	}

	/**
	 * @param research
	 *            The research level at which to grab the shield for this ShipData.
	 * @return The maximum shields for Ships of this type at the given research level.
	 */
	public int getShieldCapacity(final Research research)
	{
		// TODO - Take research into account
		return aBaseShieldCapacity;
	}

	/**
	 * @param research
	 *            The research level at which to grab the shield regeneration rate for this ShipData.
	 * @return The shield regeneration rate for Ships of this type at the given research level.
	 */
	public int getShieldRegenRate(final Research research)
	{
		// TODO - multiply based on research
		return aBaseShieldRegen;
	}

	/**
	 * @return The factor by which the shield sprite is scaled.
	 */
	public float getShieldScale()
	{
		return aShieldScale;
	}

	/**
	 * @param research
	 *            The research level at which to grab the shield sprite for this ShipData.
	 * @return The shield sprite for Ships of this type at the given research level.
	 */
	public SpriteData getShieldSprite(final Research research)
	{
		return aRace.getShieldSprite(research, getDimension());
	}

	/**
	 * @param research
	 *            The research level at which to grab the speed of this ShipData.
	 * @return The movement speed for Ships of this type at the given research level.
	 */
	public int getSpeed(final Research research)
	{
		return aBaseSpeed;
	}

	/**
	 * @return The in game name of this Ship type.
	 */
	public String getTitle()
	{
		return aTitle;
	}

	/**
	 * @return The offsets at which the trail sprites are placed.
	 */
	public List<Point> getTrailOffsets()
	{
		return aTrailOffset;
	}

	/**
	 * @return This Ship type.
	 */
	public String getType()
	{
		return aType;
	}

	/**
	 * @return he offsets at which the weapons sprites are placed.
	 */
	public List<Point> getWeaponSlots()
	{
		return aWeaponSlots;
	}

	/**
	 * @param weapon
	 *            the type of weapon whose sprite we are looking for.
	 * @return The turret sprites for the given weapon.
	 */
	public List<SpriteData> getWeaponSprites(final WeaponData weapon)
	{
		final List<SpriteData> list = new ArrayList<SpriteData>(aWeaponSlots.size());
		int index = 0;
		for (final Point p : aWeaponSlots) {
			list.add(new SpriteData(weapon.getTurretSprite(index), p.x, p.y));
			index++;
		}
		return list;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setAttribute("dimension", aDimension);
		j.setAttribute("speed", aBaseSpeed);
		j.setListAttribute("engineoffset", aEngineOffset);
		j.setAttribute("movingtime", aMovingTime);
		j.setAttribute("rotationspeed", aRotationSpeed);
		j.setListAttribute("trailoffset", aTrailOffset);
		j.setAttribute("basehealth", aBaseHealthCapacity);
		j.setAttribute("basedamage", aBaseDamage);
		j.setAttribute("canshoot", aCanShoot);
		j.setAttribute("title", aTitle);
		j.setAttribute("cost", aBaseCost);
		j.setAttribute("buildTime", aBaseBuildTime);
		j.setAttribute("baseRadiation", aBaseRadiationCapacity);
		j.setAttribute("baseShields", aBaseShieldCapacity);
		j.setAttribute("shieldRegen", aBaseShieldRegen);
		j.setAttribute("healthRegen", aBaseShieldRegen);
		j.setAttribute("cargoCapacity", aBaseCargoCapacity);
		j.setAttribute("dockingSize", aBaseDockingSize);
		j.setListAttribute("weapons", aWeaponSlots);
		j.setAttribute("shieldscale", aShieldScale);
		return j;
	}
}

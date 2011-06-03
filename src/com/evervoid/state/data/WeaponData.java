package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.player.Research;
import com.evervoid.state.prop.Ship;

/**
 * WeaponsData represents the core data needed to serialize the weapons of a {@link Ship}. It determines how much damage the
 * weapons do, where they are located, how the look.... WeaponsData are specific to their ShipData, and represent the base
 * weapons, which can then be improved through {@link Research}.
 */
public class WeaponData implements Jsonable
{
	/**
	 * The name of the race that can build these weapons.
	 */
	private final String aRace;
	/**
	 * The time interval between shots fired from this weapon.
	 */
	private final float aShotInterval;
	/**
	 * The number of shots fired by one volley.
	 */
	private final int aShots;
	/**
	 * The speed of the shots fired.
	 */
	private final float aShotSpeed;
	/**
	 * The number of turrets this weapon is comprised of.
	 */
	private final int aTurrets;
	/**
	 * The String representation of this WeaponData.
	 */
	private final String aType;

	/**
	 * Creates a WeaponData from the given parameters.
	 * 
	 * @param type
	 *            The type of weapon.
	 * @param race
	 *            The race capable of building these weapons.
	 * @param j
	 *            The Json containing the information pertinent to these weapons.
	 */
	WeaponData(final String type, final String race, final Json j)
	{
		aType = type;
		aRace = race;
		aTurrets = j.getIntAttribute("turrets");
		aShots = j.getIntAttribute("shots");
		aShotInterval = j.getFloatAttribute("interval");
		aShotSpeed = j.getFloatAttribute("speed");
	}

	/**
	 * @return The Sprite for this weapon's lasers.
	 */
	public SpriteData getLaserSprite()
	{
		return new SpriteData("ships/" + aRace + "/" + aType + "/laser.png");
	}

	/**
	 * @return The number of shots fired by this weapon.
	 */
	public int getShotCount()
	{
		return aShots;
	}

	/**
	 * @return The time between shots from this weapon.
	 */
	public float getShotInterval()
	{
		return aShotInterval;
	}

	/**
	 * @return The speed at which this weapon fires.
	 */
	public float getShotSpeed()
	{
		return aShotSpeed;
	}

	/**
	 * @return The number of turrets on this weapon.
	 */
	public int getTurretCount()
	{
		return aTurrets;
	}

	/**
	 * @return THe sprite for the turret at the given slot.
	 */
	public String getTurretSprite(final int turretSlot)
	{
		if (turretSlot < 0 || turretSlot >= aTurrets) {
			return null;
		}
		return "ships/" + aRace + "/" + aType + "/turret_" + turretSlot + ".png";
	}

	/**
	 * @return This weapon's type.
	 */
	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("turrets", aTurrets).setAttribute("shots", aShots)
				.setAttribute("interval", aShotInterval).setAttribute("speed", aShotSpeed);
	}
}

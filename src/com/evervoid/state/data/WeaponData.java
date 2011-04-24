package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class WeaponData implements Jsonable
{
	private final float aInterval;
	private final String aRace;
	private final int aShots;
	private final float aSpeed;
	private final int aTurrets;
	private final String aType;

	WeaponData(final String type, final String race, final Json j)
	{
		aType = type;
		aRace = race;
		aTurrets = j.getIntAttribute("turrets");
		aShots = j.getIntAttribute("shots");
		aInterval = j.getFloatAttribute("interval");
		aSpeed = j.getFloatAttribute("speed");
	}

	public float getInterval()
	{
		return aInterval;
	}

	public SpriteData getLaserSprite()
	{
		return new SpriteData("ships/" + aRace + "/" + aType + "/laser.png");
	}

	public int getShots()
	{
		return aShots;
	}

	public float getSpeed()
	{
		return aSpeed;
	}

	public int getTurrets()
	{
		return aTurrets;
	}

	public String getTurretSprite(final int turret)
	{
		if (turret < 0 || turret >= aTurrets) {
			return null;
		}
		return "ships/" + aRace + "/" + aType + "/turret_" + turret + ".png";
	}

	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("turrets", aTurrets).setAttribute("shots", aShots)
				.setAttribute("interval", aInterval).setAttribute("speed", aSpeed);
	}
}

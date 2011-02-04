package com.evervoid.gamedata;

import java.util.HashMap;
import java.util.Map;

public class RaceData
{
	// Note: This should stay implemented as an enum.
	// Lots of complications if that's not the case
	public enum Race
	{
		ROUND, SQUARE, TRIANGLE;
	}

	private static final Map<String, RaceData> sInstances = new HashMap<String, RaceData>();

	public static RaceData getRaceData(final String raceType)
	{
		if (!sInstances.containsKey(raceType)) {
			sInstances.put(raceType, new RaceData(raceType));
		}
		return sInstances.get(raceType);
	}

	private final Race aRace;

	private RaceData(final String race)
	{
		aRace = Race.valueOf(race);
	}

	public String getBaseSprite()
	{
		switch (aRace) {
			case ROUND:
				return "planets/gas/planet_gas_1.png";
		}
		return "";
	}

	public Race getRace()
	{
		return aRace;
	}

	public String getType()
	{
		return aRace.toString();
	}
}

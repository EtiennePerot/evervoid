package com.evervoid.gamedata;

import com.evervoid.state.Dimension;

public class PlanetData
{
	// Note: This is currently implemented as an enum, but let's not rely on it
	// in case we switch to XML-based parsing
	private enum PlanetType
	{
		ORANGETHINGY;
	}

	private final PlanetType aType;

	public PlanetData(final String ship)
	{
		aType = PlanetType.valueOf(ship);
	}

	public String getBaseSprite()
	{
		switch (aType) {
			case ORANGETHINGY:
				return "planets/gas/planet_gas_1.png";
		}
		return "";
	}

	public Dimension getDimension()
	{
		switch (aType) {
			case ORANGETHINGY:
				return new Dimension(2, 2);
		}
		return null;
	}

	public String getType()
	{
		return aType.toString();
	}
}

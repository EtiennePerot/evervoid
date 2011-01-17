package com.evervoid.gamedata;

public class ShipData
{
	// Note: This is currently implemented as an enum, but let's not rely on it
	// in case we switch to XML-based parsing
	private enum ShipType
	{
		BIGASS, SCOUT;
	}

	private final ShipType aType;

	public ShipData(final String ship)
	{
		aType = ShipType.valueOf(ship);
	}

	public String getBaseSprite()
	{
		switch (aType) {
			case SCOUT:
				return "ships/square/scout_base.png";
			case BIGASS:
				return "ships/round/bigship_base.png";
		}
		return "";
	}

	public String getColorOverlay()
	{
		switch (aType) {
			case SCOUT:
				return "ships/square/scout_color.png";
			case BIGASS:
				return "ships/round/bigship_color.png";
		}
		return "";
	}

	public Dimension getDimension()
	{
		switch (aType) {
			case SCOUT:
				return new Dimension();
			case BIGASS:
				return new Dimension(2, 2);
		}
		return null;
	}

	public float getMovingTime()
	{
		switch (aType) {
			case SCOUT:
				return 0.75f;
			case BIGASS:
				return 3f;
		}
		return 1f;
	}

	public float getRotationSpeed()
	{
		switch (aType) {
			case SCOUT:
				return 2f;
			case BIGASS:
				return 0.5f;
		}
		return 1f;
	}
}

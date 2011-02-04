package com.evervoid.gamedata;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.state.Dimension;
import com.evervoid.state.Point;

public class ShipData
{
	// Note: This is currently implemented as an enum, but let's not rely on it
	// in case we switch to XML-based parsing
	private enum ShipType
	{
		BIGASS, SCOUT;
	}

	private static final Map<String, ShipData> sInstances = new HashMap<String, ShipData>();

	public static ShipData getShipData(final String shipType)
	{
		if (!sInstances.containsKey(shipType)) {
			sInstances.put(shipType, new ShipData(shipType));
		}
		return sInstances.get(shipType);
	}

	private final ShipType aType;

	private ShipData(final String shipType)
	{
		aType = ShipType.valueOf(shipType);
	}

	public SpriteInfo getBaseSprite()
	{
		switch (aType) {
			case SCOUT:
				return new SpriteInfo("ships/square/scout_base.png");
			case BIGASS:
				return new SpriteInfo("ships/round/bigship_base.png");
		}
		return null;
	}

	public SpriteInfo getColorOverlay()
	{
		switch (aType) {
			case SCOUT:
				return new SpriteInfo("ships/square/scout_color.png");
			case BIGASS:
				return new SpriteInfo("ships/round/bigship_color.png");
		}
		return null;
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

	public Point getEngineOffset()
	{
		switch (aType) {
			case SCOUT:
				return new Point(28, 0);
			case BIGASS:
				return new Point(2, 0);
		}
		return null;
	}

	public float getMovingTime()
	{
		switch (aType) {
			case SCOUT:
				return 0.75f;
			case BIGASS:
				return 2f;
		}
		return 1f;
	}

	public float getRotationSpeed()
	{
		switch (aType) {
			case SCOUT:
				return 2f;
			case BIGASS:
				return 0.75f;
		}
		return 1f;
	}

	public Point getTrailAttachPoint()
	{
		switch (aType) {
			case SCOUT:
				return new Point(32, 0);
			case BIGASS:
				return new Point(18, 0);
		}
		return null;
	}

	public String getType()
	{
		return aType.toString();
	}
}

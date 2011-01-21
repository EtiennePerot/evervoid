package com.evervoid.state;

import java.util.HashMap;
import java.util.Map;

public class Galaxy
{
	private final Map<Point3D, SolarSystem> fSolarMap;

	protected Galaxy(final Map<Point3D, SolarSystem> pMap)
	{
		fSolarMap = pMap;
	}

	protected Galaxy copy()
	{
		final Map<Point3D, SolarSystem> tempMap = new HashMap<Point3D, SolarSystem>();
		// TODO, actually clone the solar systems
		tempMap.putAll(fSolarMap);
		return new Galaxy(tempMap);
	}

	protected SolarSystem getSolarSystem(final Point3D point)
	{
		return fSolarMap.get(point);
	}
}

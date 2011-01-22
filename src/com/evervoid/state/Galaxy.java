package com.evervoid.state;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Galaxy
{
	private final BiMap<SolarSystem, Point3D> fSolarMap;
	private final Set<Wormhole> fWormholeList;

	protected Galaxy(final Map<SolarSystem, Point3D> pMap, final Map<SolarSystem, SolarSystem> wormholes)
	{
		fSolarMap = new BiMap<SolarSystem, Point3D>(pMap);
		fWormholeList = new HashSet<Wormhole>();
		for (final SolarSystem s1 : wormholes.keySet()) {
			if (fSolarMap.contains(s1) && fSolarMap.contains(wormholes.get(s1))) {
				final Point3D location1 = fSolarMap.get2(s1);
				final Point3D location2 = fSolarMap.get2(wormholes.get(s1));
				fWormholeList.add(new Wormhole(location1, location2));
			}
		}
	}

	protected SolarSystem getSolarSystem(final Point3D point)
	{
		return fSolarMap.get1(point);
	}
}

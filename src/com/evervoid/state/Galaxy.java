package com.evervoid.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.jme3.math.FastMath;

public class Galaxy
{
	public static Galaxy createRandomGalaxy()
	{
		final Map<SolarSystem, Point3D> tempMap = createRandomSolarMap();
		final Map<SolarSystem, SolarSystem> tempWormholes = new HashMap<SolarSystem, SolarSystem>();
		final Galaxy tempGalaxy = new Galaxy(tempMap, tempWormholes);
		return tempGalaxy;
	}

	private static Map<SolarSystem, Point3D> createRandomSolarMap()
	{
		final Map<SolarSystem, Point3D> tMap = new HashMap<SolarSystem, Point3D>();
		for (int i = 0; i < 5; i++) {
			final Point3D tPoint = new Point3D(FastMath.rand.nextInt(100) - 50, FastMath.rand.nextInt(100) - 50,
					FastMath.rand.nextInt(100) - 50);
			final SolarSystem tSolar = SolarSystem.createRandomSolarSystem();
			tMap.put(tSolar, tPoint);
		}
		return tMap;
	}

	private int aSize = 0;
	private final BiMap<SolarSystem, Point3D> fSolarMap;
	private final Set<Wormhole> fWormholeList;

	protected Galaxy(final Map<SolarSystem, Point3D> pMap, final Map<SolarSystem, SolarSystem> wormholes)
	{
		fSolarMap = new BiMap<SolarSystem, Point3D>();
		for (final SolarSystem ss : pMap.keySet()) {
			addSolarSystem(ss, pMap.get(ss));
		}
		fWormholeList = new HashSet<Wormhole>();
		for (final SolarSystem s1 : wormholes.keySet()) {
			if (fSolarMap.contains(s1) && fSolarMap.contains(wormholes.get(s1))) {
				final Point3D location1 = fSolarMap.get2(s1);
				final Point3D location2 = fSolarMap.get2(wormholes.get(s1));
				fWormholeList.add(new Wormhole(location1, location2));
			}
		}
	}

	private void addSolarSystem(final SolarSystem pSolar, final Point3D pPoint)
	{
		// TODO use a clone
		fSolarMap.put(pSolar, pPoint);
		// if new solar system is out of bounds, resize
		aSize = Math.max(aSize, pSolar.getSize() + (int) pPoint.getDistanceToOrigin());
	}

	public int getSize()
	{
		return aSize;
	}

	public Set<Point3D> getSolarPoints()
	{
		return fSolarMap.keySet2();
	}

	public SolarSystem getSolarSystem(final Point3D point)
	{
		return fSolarMap.get1(point);
	}
}

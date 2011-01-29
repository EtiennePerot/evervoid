package com.evervoid.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.jme3.math.FastMath;

/**
 * This class represents a physical galaxy consisting of Solar Systems, Wormholes, etc.
 */
public class Galaxy
{
	/**
	 * @return A randomly generated galaxy
	 */
	public static Galaxy createRandomGalaxy()
	{
		final Map<SolarSystem, Point3D> tempMap = createRandomSolarMap();
		final Map<SolarSystem, SolarSystem> tempWormholes = new HashMap<SolarSystem, SolarSystem>();
		final Galaxy tempGalaxy = new Galaxy(tempMap, tempWormholes);
		return tempGalaxy;
	}

	/**
	 * @return A map of randomly generated solar systems.
	 */
	private static Map<SolarSystem, Point3D> createRandomSolarMap()
	{
		final Map<SolarSystem, Point3D> tMap = new HashMap<SolarSystem, Point3D>();
		for (int i = 0; i < 5; i++) {
			final Point3D tPoint = new Point3D(FastMath.rand.nextInt(50), FastMath.rand.nextInt(50), FastMath.rand.nextInt(500));
			final SolarSystem tSolar = SolarSystem.createRandomSolarSystem();
			tMap.put(tSolar, tPoint);
		}
		return tMap;
	}

	private int aSize = 0;
	private final BiMap<SolarSystem, Point3D> fSolarMap;
	private final Set<Wormhole> fWormholeList;

	/**
	 * Protected constructor used to create a galaxy using a solar system and wormhole map.
	 * 
	 * @param pMap
	 *            A map containing the location of solar systems.
	 * @param wormholes
	 *            A map containing the links (wormholes) between solar systems.
	 */
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

	/**
	 * Add a solar system to the galaxy
	 * 
	 * @param pSolar
	 *            Solar system to add.
	 * @param pPoint
	 *            Point describing the location of the solar system in space.
	 */
	private void addSolarSystem(final SolarSystem pSolar, final Point3D pPoint)
	{
		// TODO use a clone
		fSolarMap.put(pSolar, pPoint);
		// if new solar system is out of bounds, resize
		aSize = Math.max(aSize, pSolar.getHeight() + (int) pPoint.getDistanceToOrigin());
	}

	/**
	 * @return The size of the galaxy.
	 */
	public int getSize()
	{
		return aSize;
	}

	/**
	 * @return A set containing the position of the solar systems.
	 */
	public Set<Point3D> getSolarPoints()
	{
		return fSolarMap.keySet2();
	}

	/**
	 * @param point
	 *            3D Point in space.
	 * @return The solar system located at this point.
	 */
	public SolarSystem getSolarSystem(final Point3D point)
	{
		return fSolarMap.get1(point);
	}
}

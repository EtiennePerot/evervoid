package com.evervoid.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.jme3.math.FastMath;

/**
 * This class represents a physical galaxy consisting of Solar Systems, Wormholes, etc.
 */
public class Galaxy implements Jsonable
{
	/**
	 * @return A randomly generated galaxy
	 */
	public static Galaxy createRandomGalaxy(final EverVoidGameState state)
	{
		final Map<SolarSystem, Point3D> tempMap = createRandomSolarMap(state);
		final Map<SolarSystem, SolarSystem> tempWormholes = new HashMap<SolarSystem, SolarSystem>();
		final Galaxy tempGalaxy = new Galaxy(tempMap, tempWormholes);
		return tempGalaxy;
	}

	/**
	 * @return A map of randomly generated solar systems.
	 */
	private static Map<SolarSystem, Point3D> createRandomSolarMap(final EverVoidGameState state)
	{
		final Map<SolarSystem, Point3D> tMap = new HashMap<SolarSystem, Point3D>();
		for (int i = 0; i < 5; i++) {
			final Point3D tPoint = new Point3D(FastMath.rand.nextInt(100) - 50, FastMath.rand.nextInt(100) - 50,
					FastMath.rand.nextInt(100) - 50);
			final SolarSystem tSolar = SolarSystem.createRandomSolarSystem(state);
			tMap.put(tSolar, tPoint);
		}
		return tMap;
	}

	private int aSize = 0;
	private final BiMap<SolarSystem, Point3D> aSolarMap;
	/**
	 * Temporary solar system; remove!
	 */
	private SolarSystem aTempSolarSystem = null;
	private final Set<Wormhole> aWormholeList;

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
		aSolarMap = new BiMap<SolarSystem, Point3D>();
		for (final SolarSystem ss : pMap.keySet()) {
			addSolarSystem(ss, pMap.get(ss));
		}
		aWormholeList = new HashSet<Wormhole>();
		for (final SolarSystem s1 : wormholes.keySet()) {
			if (aSolarMap.contains(s1) && aSolarMap.contains(wormholes.get(s1))) {
				final Point3D location1 = aSolarMap.get2(s1);
				final Point3D location2 = aSolarMap.get2(wormholes.get(s1));
				aWormholeList.add(new Wormhole(location1, location2));
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
		if (aTempSolarSystem == null) {
			aTempSolarSystem = pSolar;
		}
		// TODO use a clone
		aSolarMap.put(pSolar, pPoint);
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
		return aSolarMap.keySet2();
	}

	/**
	 * @param point
	 *            3D Point in space.
	 * @return The solar system located at this point.
	 */
	public SolarSystem getSolarSystem(final Point3D point)
	{
		return aSolarMap.get1(point);
	}

	SolarSystem getTempSolarSystem()
	{
		return aTempSolarSystem;
	}

	@Override
	public Json toJson()
	{
		final List<Json> solars = new ArrayList<Json>(aSolarMap.size());
		for (final SolarSystem ss : aSolarMap.keySet1()) {
			solars.add(new Json().setAttribute("point", aSolarMap.get2(ss)).setAttribute("solar", ss));
		}
		return new Json().setIntAttribute("size", aSize).setListAttribute("solarsystems", solars);
	}
}

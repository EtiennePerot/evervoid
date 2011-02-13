package com.evervoid.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.jme3.math.FastMath;

/**
 * This class represents a physical galaxy consisting of Solar Systems, Wormholes, etc.
 */
public class Galaxy implements Jsonable
{
	/**
	 * Restores a Galaxy from a serialized representation
	 * 
	 * @param j
	 *            Serialized representation of galaxy
	 * @return Deserialized Galaxy object
	 */
	protected static Galaxy fromJson(final Json j, final EverVoidGameState state)
	{
		final Map<SolarSystem, Point3D> solarMap = new HashMap<SolarSystem, Point3D>();
		for (final Json ss : j.getListAttribute("solarsystems")) {
			solarMap.put(new SolarSystem(ss, state), Point3D.fromJson(ss.getAttribute("point")));
		}
		final Map<SolarSystem, SolarSystem> wormHoles = new HashMap<SolarSystem, SolarSystem>();
		// TODO: Populate wormholes
		return new Galaxy(solarMap, wormHoles, state);
	}

	private int aSize = 0;
	private final BiMap<SolarSystem, Point3D> aSolarMap = new BiMap<SolarSystem, Point3D>();
	private final EverVoidGameState aState;
	/**
	 * Temporary solar system; remove!
	 */
	private SolarSystem aTempSolarSystem = null;
	private final Set<Wormhole> aWormholes = new HashSet<Wormhole>();

	/**
	 * Base constructor
	 * 
	 * @param state
	 *            The EverVoidGameState that hosts this Galaxy
	 */
	protected Galaxy(final EverVoidGameState state)
	{
		aState = state;
	}

	/**
	 * Protected constructor used to create a galaxy using a solar system and wormhole map.
	 * 
	 * @param pMap
	 *            A map containing the location of solar systems.
	 * @param wormholes
	 *            A map containing the links (wormholes) between solar systems.
	 */
	protected Galaxy(final Map<SolarSystem, Point3D> pMap, final Map<SolarSystem, SolarSystem> wormholes,
			final EverVoidGameState state)
	{
		this(state);
		for (final SolarSystem ss : pMap.keySet()) {
			addSolarSystem(ss, pMap.get(ss));
		}
		for (final SolarSystem ss1 : wormholes.keySet()) {
			addWormhole(ss1, wormholes.get(ss1));
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
		aSolarMap.put(pSolar, pPoint);
		// if new solar system is out of bounds, resize
		aSize = Math.max(aSize, pSolar.getHeight() + (int) pPoint.getDistanceToOrigin());
	}

	/**
	 * Adds a wormhole going from a solar system to another. If the two specified solar systems were already connected by a
	 * wormhole, the existing wormhole is not overwritten (nothing happens)
	 * 
	 * @param ss1
	 *            The first solar system
	 * @param ss2
	 *            The second solar system
	 */
	protected void addWormhole(final SolarSystem ss1, final SolarSystem ss2)
	{
		if (!ss1.equals(ss2) && getConnection(ss1, ss2) == null) {
			aWormholes.add(new Wormhole(ss1, ss2, aState));
		}
	}

	/**
	 * @param ss1
	 *            One of the two solar systems
	 * @param ss2
	 *            One of the two solar systems
	 * @return The wormhole connecting the specified solar systems, or null if there is no connection
	 */
	public Wormhole getConnection(final SolarSystem ss1, final SolarSystem ss2)
	{
		if (ss1.equals(ss2)) {
			// Can't connect a solar system to itself
			return null;
		}
		for (final Wormhole w : aWormholes) {
			if (w.connects(ss1, ss2)) {
				return w;
			}
		}
		return null;
	}

	/**
	 * @return A guaranteed-to-be-unique ID for solar systems
	 */
	int getNewSolarID()
	{
		int max = 0;
		for (final SolarSystem ss : aSolarMap.keySet1()) {
			if (ss.getID() > max) {
				max = ss.getID();
			}
		}
		return max + 1;
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

	/**
	 * @param ss1
	 *            First solar system
	 * @param ss2
	 *            Second solar system
	 * @return Spatial distance between the two specified solar systems
	 */
	public double getSolarSystemDistance(final SolarSystem ss1, final SolarSystem ss2)
	{
		if (aSolarMap.contains(ss1) && aSolarMap.contains(ss2)) {
			return aSolarMap.get2(ss1).distanceTo(aSolarMap.get2(ss2));
		}
		return 0;
	}

	SolarSystem getTempSolarSystem()
	{
		return aTempSolarSystem;
	}

	/**
	 * Randomly adds solar systems and wormholes to this galaxy
	 */
	void populateRandomly()
	{
		for (int i = 0; i < 5; i++) {
			final Point3D tPoint = new Point3D(FastMath.rand.nextInt(100) - 50, FastMath.rand.nextInt(100) - 50,
					FastMath.rand.nextInt(100) - 50);
			final int width = MathUtils.getRandomIntBetween(32, 128);
			final int height = MathUtils.getRandomIntBetween(24, 72);
			final SolarSystem tSolar = new SolarSystem(new Dimension(width, height), getNewSolarID(), aState);
			tSolar.populateRandomly();
			addSolarSystem(tSolar, tPoint);
		}
		for (int i = 0; i < 20; i++) {
			addWormhole(aSolarMap.getRandom1(), aSolarMap.getRandom1());
		}
		// TODO: Make wormholes
	}

	@Override
	public Json toJson()
	{
		final List<Json> solars = new ArrayList<Json>(aSolarMap.size());
		for (final SolarSystem ss : aSolarMap.keySet1()) {
			solars.add(new Json().setAttribute("point", aSolarMap.get2(ss)).setAttribute("solar", ss));
		}
		return new Json().setListAttribute("solarsystems", solars).setListAttribute("wormholes", aWormholes);
	}
}

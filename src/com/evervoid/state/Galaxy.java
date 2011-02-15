package com.evervoid.state;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

/**
 * This class represents a physical galaxy consisting of Solar Systems, Wormholes, etc.
 */
public class Galaxy implements Jsonable
{
	private int aSize = 0;
	private final Map<Integer, SolarSystem> aSolarSystems = new HashMap<Integer, SolarSystem>();
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
	 * Add a solar system to the galaxy
	 * 
	 * @param pSolar
	 *            Solar system to add.
	 * @param pPoint
	 *            Point describing the location of the solar system in space.
	 */
	private void addSolarSystem(final SolarSystem pSolar)
	{
		if (aTempSolarSystem == null) {
			aTempSolarSystem = pSolar;
		}
		aSolarSystems.put(pSolar.getID(), pSolar);
		// if new solar system is out of bounds, resize
		aSize = Math.max(aSize, pSolar.getRadius() + (int) pSolar.getPoint3D().getDistanceToOrigin());
	}

	/**
	 * Adds an empty (no ships) wormhole going from a solar system to another. If the two specified solar systems were already
	 * connected by a wormhole, the existing wormhole is not overwritten (nothing happens)
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
	 * @return A new, unused solar system ID
	 */
	public int getNextSolarID()
	{
		// If we have no solar system, then ID 0 is not taken
		if (aSolarSystems.isEmpty()) {
			return 0;
		}
		// If we have solar systems, iterate over them, get the max, and return max+1
		// because that ID is certainly not taken
		int maxId = Integer.MIN_VALUE;
		for (final Integer id : aSolarSystems.keySet()) {
			maxId = Math.max(maxId, id);
		}
		return maxId + 1;
	}

	/**
	 * Finds a random, non-overlapping Point3D in space
	 * 
	 * @param radius
	 *            The radius of the sphere occupying the Point3D
	 * @return A Point3D at which a sphere of the given radius would not overlap with anything
	 */
	private Point3D getRandomSolarPoint(final int radius)
	{
		// Might want to change the min/max values here
		Point3D point = null;
		while (point == null || isOccupied(point, radius)) {
			point = new Point3D(MathUtils.getRandomIntBetween(-500, 500), MathUtils.getRandomIntBetween(-500, 500),
					MathUtils.getRandomIntBetween(-500, 500));
		}
		return point;
	}

	/**
	 * @return The size of the galaxy.
	 */
	public int getSize()
	{
		return aSize;
	}

	/**
	 * @return The set of all Point3D of each solar system
	 */
	public Set<Point3D> getSolarPoints()
	{
		final Set<Point3D> points = new HashSet<Point3D>();
		for (final SolarSystem ss : aSolarSystems.values()) {
			points.add(ss.getPoint3D());
		}
		return points;
	}

	/**
	 * @param id
	 *            The ID of the solar system
	 * @return The solar system of the specified ID.
	 */
	public SolarSystem getSolarSystem(final int id)
	{
		return aSolarSystems.get(id);
	}

	/**
	 * @param point
	 * @return
	 */
	public SolarSystem getSolarSystemByPoint3D(final Point3D point)
	{
		for (final SolarSystem ss : aSolarSystems.values()) {
			if (ss.getPoint3D().sameAs(point)) {
				return ss;
			}
		}
		return null;
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
		if (aSolarSystems.containsValue(ss1) && aSolarSystems.containsValue(ss2)) {
			return ss1.getPoint3D().distanceTo(ss2.getPoint3D());
		}
		return 0;
	}

	/**
	 * @return All solar systems
	 */
	public Collection<SolarSystem> getSolarSystems()
	{
		return aSolarSystems.values();
	}

	SolarSystem getTempSolarSystem()
	{
		return aTempSolarSystem;
	}

	/**
	 * @return The set of wormholes in this galaxy
	 */
	public Iterable<Wormhole> getWormholes()
	{
		return aWormholes;
	}

	/**
	 * Finds if a hypothetical sphere at the given Point3D with the given radius would colliede with the existing solar systems
	 * 
	 * @param point
	 *            The center of the sphere
	 * @param radius
	 *            The radius of the sphere
	 * @return Whether the given sphere would collide or not
	 */
	public boolean isOccupied(final Point3D point, final int radius)
	{
		for (final SolarSystem ss : aSolarSystems.values()) {
			if (ss.getPoint3D().distanceTo(point) <= radius + ss.getRadius()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Populates the Galaxy from serialized Galaxy representation
	 * 
	 * @param j
	 *            Serialized representation of galaxy
	 */
	void populate(final Json j)
	{
		final Json solarsystems = j.getAttribute("solarsystems");
		for (final String ss : solarsystems.getAttributes()) {
			addSolarSystem(new SolarSystem(solarsystems.getAttribute(ss), aState));
		}
		for (final Json wormhole : j.getListAttribute("wormholes")) {
			aWormholes.add(new Wormhole(wormhole, aState));
		}
	}

	/**
	 * Randomly adds solar systems and wormholes to this galaxy
	 */
	void populateRandomly()
	{
		for (int i = 0; i < 5; i++) {
			final int width = MathUtils.getRandomIntBetween(32, 128);
			final int height = MathUtils.getRandomIntBetween(24, 72);
			final Point3D origin = getRandomSolarPoint(Math.max(width, height));
			final SolarSystem tSolar = new SolarSystem(new Dimension(width, height), origin, aState);
			tSolar.populateRandomly();
			addSolarSystem(tSolar);
		}
		for (int i = 0; i < 20; i++) {
			addWormhole((SolarSystem) MathUtils.getRandomElement(aSolarSystems.values()),
					(SolarSystem) MathUtils.getRandomElement(aSolarSystems.values()));
		}
	}

	@Override
	public Json toJson()
	{
		return new Json().setIntMapAttribute("solarsystems", aSolarSystems).setListAttribute("wormholes", aWormholes);
	}
}

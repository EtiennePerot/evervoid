package com.evervoid.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.Point3D;
import com.evervoid.state.prop.Portal;
import com.evervoid.utils.MathUtils;

/**
 * This class represents a physical galaxy consisting of Solar Systems, Wormholes, etc.
 */
public class Galaxy implements Jsonable
{
	/**
	 * This padding establishes a sphere around a SolarSystem within which wormholes are not allowed to pass.
	 */
	private static final int sSolarPadding = 50;
	/**
	 * This is the radius of the Galaxy, it is determined by the radius and distance of the farthest SoalrSystem.
	 */
	private int aRadius = 0;
	/**
	 * A mapping from id to SolarSystem
	 */
	private final Map<Integer, SolarSystem> aSolarSystems;
	/**
	 * The state to which this Galaxy belongs
	 */
	private final EVGameState aState;
	/**
	 * Temporary solar system; remove!
	 */
	private SolarSystem aTempSolarSystem = null;
	/**
	 * A mapping of id to wormholes.
	 */
	private final Map<Integer, Wormhole> aWormholes = new HashMap<Integer, Wormhole>();

	/**
	 * Creates a new, empty Galaxy attached to the passed state.
	 * 
	 * @param state
	 *            The state to which this Galaxy will belong.
	 */
	protected Galaxy(final EVGameState state)
	{
		// prime the galaxy for solar system
		aSolarSystems = new HashMap<Integer, SolarSystem>();
		// set the state so functions can use it
		aState = state;
	}

	/**
	 * Deserializes a Galaxy Json using reversions from the passed state.
	 * 
	 * @param j
	 *            The Json representation of the Galaxy.
	 * @param state
	 *            The State containing objects to which the Galaxy will point.
	 */
	protected Galaxy(final Json j, final EVGameState state)
	{
		this(state);
		// state can only have one galaxy, this is it
		state.aGalaxy = this;
		// Step 1: Create empty solar systems
		final Json solarsystems = j.getAttribute("solarsystems");
		for (final String ss : solarsystems.getAttributes()) {
			final SolarSystem tempSystem = new SolarSystem(solarsystems.getAttribute(ss), state);
			addSolarSystem(tempSystem);
		}
		// Step 2: Create wormholes
		for (final Json wormhole : j.getListAttribute("wormholes")) {
			final Wormhole tempWormhole = new Wormhole(wormhole, state);
			addWormhole(tempWormhole);
		}
		// Step 3: Populate solar systems
		for (final SolarSystem ss : aSolarSystems.values()) {
			ss.populate(solarsystems.getAttribute(String.valueOf(ss.getID())));
		}
		// The above must be done in the established order
		// This is because Portals contain Wormholes, Wormholes contain SolarSystem, and SolarSystems contain
		// Portals. So to avoid NullPointers we must first create empty SolarSystems, then link Wormholes, and
		// finally fill the SolarSystems with their props.
	}

	/**
	 * Add a solar system to the galaxy
	 * 
	 * @param pSolar
	 *            Solar system to add.
	 */
	private void addSolarSystem(final SolarSystem pSolar)
	{
		if (aTempSolarSystem == null) {
			aTempSolarSystem = pSolar;
		}
		aSolarSystems.put(pSolar.getID(), pSolar);
		// if new solar system is out of bounds, resize
		aRadius = Math.max(aRadius, pSolar.getRadius() + (int) pSolar.getPoint3D().getDistanceToOrigin());
	}

	/**
	 * Adds an empty (no ships) wormhole going from a solar system to another. If the two specified solar systems were already
	 * connected by a wormhole, the existing wormhole is not overwritten (nothing happens)
	 * 
	 * @param wormhole
	 *            The wormhole to add
	 */
	private boolean addWormhole(final Wormhole wormhole)
	{
		if (wormhole.isRecursive() || aWormholes.containsValue(wormhole)) {
			return false;
		}
		else {
			aWormholes.put(wormhole.getID(), wormhole);
			return true;
		}
	}

	/**
	 * Adds the portals associated with a Wormhole in their respective solar systems.
	 * 
	 * @param wormhole
	 *            The Wormhole to represent using portals.
	 * @return True if and only if the portals were successfully added
	 */
	private boolean createPortals(final Wormhole wormhole)
	{
		final SolarSystem ss1 = wormhole.getSolarSystem1();
		final SolarSystem ss2 = wormhole.getSolarSystem2();
		if (!ss1.equals(ss2) && isPathClear(ss1, ss2) && addWormhole(wormhole)) {
			// Only create Portals if the Wormhole is valid!
			// Create a good Portal in the first SolarSystem
			final Portal portal1 = new Portal(aState.getNextPropID(), aState.getNullPlayer(),
					ss1.getPotentialWormholeLocation(), ss1, wormhole, aState);
			aState.registerProp(portal1, portal1.getContainer());
			// Create a good Portal in the second SolarSystem
			final Portal portal2 = new Portal(aState.getNextPropID(), aState.getNullPlayer(),
					ss2.getPotentialWormholeLocation(), ss2, wormhole, aState);
			aState.registerProp(portal2, portal2.getContainer());
			return true;
		}
		return false;
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
		for (final Wormhole w : aWormholes.values()) {
			if (w.connects(ss1, ss2)) {
				return w;
			}
		}
		return null;
	}

	/**
	 * Finds a random, non-overlapping Point3D in space
	 * 
	 * @param radius
	 *            The radius of the sphere occupying the Point3D
	 * @return A Point3D at which a sphere of the given radius would not overlap with anything
	 */
	private Point3D getNewSolarPoint(final int radius)
	{
		// Might want to change the min/max values here
		Point3D point = null;
		while (point == null || isOccupied(point, radius)) {
			point = new Point3D(MathUtils.getRandomIntBetween(-500, 500), MathUtils.getRandomIntBetween(-500, 500),
					MathUtils.getRandomIntBetween(-400, 400));
		}
		return point;
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
	 * @return i+1 where i is the greatest wormhole id currently assigned.
	 */
	public int getNextWormholeID()
	{
		int maxId = -1;
		for (final Integer id : aWormholes.keySet()) {
			maxId = Math.max(maxId, id);
		}
		return maxId + 1;
	}

	/**
	 * @return The size of the galaxy.
	 */
	public int getSize()
	{
		return aRadius;
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
	 * @return The solar system associated with the id.
	 */
	public SolarSystem getSolarSystem(final int id)
	{
		return aSolarSystems.get(id);
	}

	/**
	 * @return The SolarSystem whose center matches point, or null if there is no such SolarSystem.
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
	public float getSolarSystemDistance(final SolarSystem ss1, final SolarSystem ss2)
	{
		if (aSolarSystems.containsValue(ss1) && aSolarSystems.containsValue(ss2)) {
			return ss1.getPoint3D().distanceTo(ss2.getPoint3D());
		}
		return 0;
	}

	/**
	 * @return A list of all solar systems in this Galaxy.
	 */
	public List<SolarSystem> getSolarSystems()
	{
		return new ArrayList<SolarSystem>(aSolarSystems.values());
	}

	/**
	 * @return The wormhole associated with the given id.
	 */
	public Wormhole getWormhole(final int id)
	{
		return aWormholes.get(id);
	}

	/**
	 * @return A collection over all wormholes in this Galaxy.
	 */
	public Collection<Wormhole> getWormholes()
	{
		return aWormholes.values();
	}

	/**
	 * Finds if a hypothetical sphere at the given Point3D with the given radius would collide with the existing solar systems
	 * 
	 * @param point
	 *            The center of the sphere
	 * @param radius
	 *            The radius of the sphere
	 * @return Whether the given sphere would collide or not
	 */
	public boolean isOccupied(final Point3D point, final int radius)
	{
		final Point3D origin = new Point3D(point.x, point.y, 0);
		Point3D currentPoint = null;
		for (final SolarSystem ss : aSolarSystems.values()) {
			currentPoint = new Point3D(ss.getPoint3D().x, ss.getPoint3D().y, 0);
			if (currentPoint.distanceTo(origin) <= radius + ss.getRadius() + sSolarPadding) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds if the path representing the wormhole between 2 solar systems is clear.
	 * 
	 * @param ss1
	 *            The first solar system.
	 * @param ss2
	 *            The second solar system.
	 * @return True if the path is clear, false otherwise.
	 */
	private boolean isPathClear(final SolarSystem ss1, final SolarSystem ss2)
	{
		// TODO - I am not about to read this, add comments here
		final float x1, y1, z1, x2, y2, z2;
		float a, b, c, x3, y3, z3, r, i;
		final Point3D ss1Loc = ss1.getPoint3D();
		x1 = ss1Loc.x;
		y1 = ss1Loc.y;
		z1 = ss1Loc.z;
		final Point3D ss2Loc = ss2.getPoint3D();
		x2 = ss2Loc.x;
		y2 = ss2Loc.y;
		z2 = ss2Loc.z;
		for (final SolarSystem ss : aSolarSystems.values()) {
			if (ss.equals(ss1) || ss.equals(ss2)) {
				continue;
			}
			x3 = ss.getPoint3D().x;
			y3 = ss.getPoint3D().y;
			z3 = ss.getPoint3D().z;
			r = ss.getRadius();
			a = (float) (Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
			b = 2 * ((x2 - x1) * (x1 - x3) + (y2 - y1) * (y1 - y3) + (z2 - z1) * (z1 - z3));
			c = (float) (Math.pow(x3, 2) + Math.pow(y3, 2) + Math.pow(z3, 2) + Math.pow(x1, 2) + Math.pow(y1, 2)
					+ Math.pow(z1, 2) - 2 * (x3 * x1 + y3 * y1 + z3 * z1) - (Math.pow(r, 2) + sSolarPadding));
			i = (b * b - 4 * a * c);
			if (i >= 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Randomly adds solar systems and Wormholes to the galaxy
	 */
	public void populateGalaxy()
	{
		final int numOfSystems = (int) (aState.getNumOfPlayers() * MathUtils.getRandomFloatBetween(1.25, 1.75) + MathUtils
				.getRandomFloatBetween(2, 5));
		// create and add a bunch of random solar systems
		for (int i = 0; i < numOfSystems; i++) {
			// arbitrary bounds that benefit gameplay for now
			final int width = MathUtils.getRandomIntBetween(70, 150);
			final int height = MathUtils.getRandomIntBetween(40, 80);
			final Dimension ssDimension = new Dimension(width, height);
			// random point in galaxy
			final Point3D origin = getNewSolarPoint(Math.max(width, height));
			// add
			addSolarSystem(new SolarSystem(ssDimension, origin, aState.getRandomStar(ssDimension), aState));
		}
		final List<SolarSystem> solarSystems = getSolarSystems();
		// create and add a bunch of random Wormholes
		for (int i = 0; i < numOfSystems; i++) {
			// some will fail to add because they are recursive, but that is fine
			createPortals(new Wormhole(MathUtils.getRandomElement(solarSystems), MathUtils.getRandomElement(solarSystems),
					aState));
		}
		// now ensure connectivity
		final List<SolarSystem> ssGraph = new ArrayList<SolarSystem>();
		// This will be the root, doesn't matter which one it is
		ssGraph.add(solarSystems.remove(0));
		while (solarSystems.size() != 0) {
			// there remains an unconnected solar
			final SolarSystem curr = MathUtils.getRandomElement(solarSystems);
			boolean connected = false;
			for (int i = 0; i < ssGraph.size(); i++) {
				// connected will be true if and only if curr is connected to at least one solar system
				// within the existing graph structure.
				connected |= curr.isConnectedTo(ssGraph.get(i));
			}
			if (!connected) {
				Wormhole w = null;
				// The current SolarSystem is not part of the graph, make it so
				do {
					w = new Wormhole(curr, MathUtils.getRandomElement(ssGraph), aState);
					// this time we do need the Wormhole to be added
				}
				while (!createPortals(w));
			}
			// This Solar System is guaranteed to be in the connected graph now, move on
			solarSystems.remove(curr);
		}
	}

	/**
	 * Randomly populate all SolarSystems.
	 */
	public void populateSolarSystems()
	{
		for (final SolarSystem ss : aSolarSystems.values()) {
			ss.populateRandomly();
		}
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setMapAttribute("solarsystems", aSolarSystems);
		j.setListAttribute("wormholes", aWormholes.values());
		return j;
	}
}

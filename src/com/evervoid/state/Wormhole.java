package com.evervoid.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.observers.WormholeObserver;
import com.evervoid.state.prop.Portal;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.evervoid.utils.EVContainer;
import com.evervoid.utils.MathUtils;

public class Wormhole implements EVContainer<Prop>, Jsonable, Comparable<Wormhole>
{
	/**
	 * The number of turns to cross this Wormhole will be computed by (distance between solar systems) * this multiplier
	 */
	private static final double sDistanceToTurnMultiplier = 0.0005;
	/**
	 * The maximum number of turns any Wormhole takes to cross
	 */
	private static final int sMaximumTurns = 8;
	/**
	 * The minimum number of turns any Wormhole takes to cross
	 */
	private static final int sMinimumTurns = 1;
	/**
	 * The wormhole's unique identifier.
	 */
	private final int aID;
	/**
	 * All the objects currently observing this Wormhole.
	 */
	private final Set<WormholeObserver> aObserverSet;
	/**
	 * A Map for a Ship to it's progress along the Wormhole
	 */
	private final Map<Ship, Integer> aShipSet = new HashMap<Ship, Integer>();
	/**
	 * The state to which this Wormhole belongs.
	 */
	private final EVGameState aState;
	/**
	 * The number of turns it takes to cross this wormhole
	 */
	private final int aTurns;
	/**
	 * The id of the first solar system.
	 */
	private final Integer ssID1;
	/**
	 * The id of the second solar system.
	 */
	private final Integer ssID2;

	/**
	 * Creates a Json object from the Json object passed.
	 * 
	 * @param j
	 *            The Json from which to build.
	 * @param state
	 *            The state which will contain the solar system mappings.
	 */
	Wormhole(final Json j, final EVGameState state)
	{
		// prime Wormhole for observers
		aObserverSet = new HashSet<WormholeObserver>();
		// set state first, so that other functions may refer to it
		aState = state;
		// parse the json.
		ssID1 = j.getIntAttribute("ss1");
		ssID2 = j.getIntAttribute("ss2");
		aTurns = j.getIntAttribute("turns");
		aID = j.getIntAttribute("id");
		// contained ships
		for (final Json wormship : j.getListAttribute("ships")) {
			aShipSet.put(new Ship(wormship.getAttribute("ship"), state), wormship.getIntAttribute("progress"));
		}
	}

	/**
	 * Creates a Wormhole connecting ss1 to ss2 and of size "length".
	 * 
	 * @param ss1
	 *            The first solar system.
	 * @param ss2
	 *            The second solar system.
	 * @param state
	 *            The state to which the wormhole will belong.
	 */
	Wormhole(final SolarSystem ss1, final SolarSystem ss2, final EVGameState state)
	{
		// prime wormhole for observers
		aObserverSet = new HashSet<WormholeObserver>();
		aState = state;
		aID = state.getNextWormholeID();
		ssID1 = ss1.getID();
		ssID2 = ss2.getID();
		aTurns = MathUtils.clampInt(sMinimumTurns, (int) (getLength() * sDistanceToTurnMultiplier), sMaximumTurns);
	}

	@Override
	public boolean addElem(final Prop p)
	{
		if (!(p instanceof Ship)) {
			return false;
		}
		final Ship s = (Ship) p;
		if (aShipSet.containsKey(s)) {
			return false;
		}
		aShipSet.put(s, 0);
		return true;
	}

	@Override
	public int compareTo(final Wormhole other)
	{
		return 1000 * (getSolarSystem1().getID() - other.getSolarSystem1().getID())
				+ (getSolarSystem2().getID() - other.getSolarSystem2().getID());
	}

	/**
	 * @return True if and only if this wormhole connects the two solar system specified.
	 */
	public boolean connects(final SolarSystem ss1, final SolarSystem ss2)
	{
		// try both orderings
		return (ss1.equals(getSolarSystem1()) && ss2.equals(getSolarSystem2()))
				|| (ss1.equals(getSolarSystem2()) && ss2.equals(getSolarSystem1()));
	}

	@Override
	public boolean containsElem(final Prop p)
	{
		if (!(p instanceof Ship)) {
			return false;
		}
		final Ship s = (Ship) p;
		return aShipSet.containsKey(s);
	}

	public void deregisterObserver(final WormholeObserver wObserver)
	{
		aObserverSet.remove(wObserver);
	}

	@Override
	public Iterable<? extends Prop> elemIterator()
	{
		return aShipSet.keySet();
	}

	@Override
	public boolean equals(final Object other)
	{
		if (other == null || !(other instanceof Wormhole)) {
			return false;
		}
		final Wormhole o = (Wormhole) other;
		return connects(o.getSolarSystem1(), o.getSolarSystem2());
	}

	@Override
	public int getID()
	{
		return aID;
	}

	/**
	 * @return The distance between the two solar systems.
	 */
	public float getLength()
	{
		return getSolarSystem1().getPoint3D().distanceTo(getSolarSystem2().getPoint3D());
	}

	/**
	 * @return The opposite portal of the one passed, or null if the parameter is not part of the wormholes.
	 */
	public Portal getOtherPortal(final Portal portal)
	{
		if (portal.equals(getPortal1())) {
			return getPortal2();
		}
		else if (portal.equals(getPortal2())) {
			return getPortal1();
		}
		else {
			return null;
		}
	}

	/**
	 * @return The portal associated with the first solar system.
	 */
	public Portal getPortal1()
	{
		return getSolarSystem1().getPortalTo(getSolarSystem2());
	}

	/**
	 * @return The portal associated with the first solar system.
	 */
	public Portal getPortal2()
	{
		return getSolarSystem2().getPortalTo(getSolarSystem1());
	}

	/**
	 * @return The "first" solar system.
	 */
	public SolarSystem getSolarSystem1()
	{
		return aState.getSolarSystem(ssID1);
	}

	/**
	 * @return The "second" solar system.
	 */
	public SolarSystem getSolarSystem2()
	{
		return aState.getSolarSystem(ssID2);
	}

	/**
	 * @return true if and only if the first solar system's identifier is equal to that of the second.
	 */
	public boolean isRecursive()
	{
		return ssID1 == ssID2;
	}

	/**
	 * Increments the ship's progress based on its wormhole speed.
	 * 
	 * @param ship
	 *            The ship to move.
	 */
	public void moveShip(final Ship ship)
	{
		aShipSet.put(ship, aShipSet.get(ship) + ship.getWormholeSpeed());
		// TODO if progress >= aTurns, poke the ship
	}

	public void registerObserver(final WormholeObserver wObserver)
	{
		aObserverSet.add(wObserver);
	}

	@Override
	public boolean removeElem(final Prop p)
	{
		if (!(p instanceof Ship)) {
			return false;
		}
		final Ship s = (Ship) p;
		if (aShipSet.containsValue(s)) {
			aShipSet.remove(s);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public Json toJson()
	{
		final List<Json> ships = new ArrayList<Json>(aShipSet.size());
		for (final Ship s : aShipSet.keySet()) {
			ships.add(new Json().setAttribute("progress", aShipSet.get(s)).setAttribute("ship", s));
		}
		final Json j = new Json();
		j.setAttribute("ss1", ssID1);
		j.setAttribute("ss2", ssID2);
		j.setAttribute("turns", aTurns);
		j.setListAttribute("ships", ships);
		j.setAttribute("id", aID);
		return j;
	}
}

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
import com.evervoid.state.observers.WormholeObserver;
import com.evervoid.state.prop.Portal;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;

public class Wormhole implements EVContainer<Prop>, Jsonable, Comparable<Wormhole>
{
	/**
	 * The number of turns to cross this wormhole will be computed by (distance between solar systems) * this multiplier
	 */
	private static final double sDistanceToTurnMultiplier = 0.0005;
	/**
	 * The maximum number of turns any wormhole takes to cross
	 */
	private static final int sMaximumTurns = 8;
	/**
	 * The minimum number of turns any wormhole takes to cross
	 */
	private static final int sMinimumTurns = 1;
	private final int aID;
	private final Set<WormholeObserver> aObserverSet;
	/**
	 * A Map for a Ship to it's progress along the wormhole
	 */
	private final Map<Ship, Integer> aShipSet = new HashMap<Ship, Integer>();
	private final EVGameState aState;
	/**
	 * The number of turns it takes to cross this wormhole
	 */
	private final int aTurns;
	private Integer portalID1;
	private Integer portalID2;
	private final Integer ssID1;
	private final Integer ssID2;

	Wormhole(final Json j, final EVGameState state)
	{
		aState = state;
		ssID1 = j.getIntAttribute("ss1");
		ssID2 = j.getIntAttribute("ss2");
		portalID1 = j.getIntAttribute("p1");
		portalID2 = j.getIntAttribute("p2");
		aTurns = j.getIntAttribute("turns");
		aID = j.getIntAttribute("id");
		for (final Json wormship : j.getListAttribute("ships")) {
			aShipSet.put(new Ship(wormship.getAttribute("ship"), state.getPlayerByName(j.getStringAttribute("player")), state),
					wormship.getIntAttribute("progress"));
		}
		aObserverSet = new HashSet<WormholeObserver>();
	}

	Wormhole(final SolarSystem ss1, final SolarSystem ss2, final float length, final EVGameState state)
	{
		aState = state;
		aID = state.getNextWormholeID();
		ssID1 = ss1.getID();
		ssID2 = ss2.getID();
		aTurns = MathUtils.clampInt(sMinimumTurns, (int) (length * sDistanceToTurnMultiplier), sMaximumTurns);
		aObserverSet = new HashSet<WormholeObserver>();
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
	 * @param ss1
	 *            One of the two solar systems
	 * @param ss2
	 *            One of the two solar systems
	 * @return Whether this wormhole connects the specified solar systems or not
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

	public Portal getPortal1()
	{
		return (Portal) aState.getPropFromID(portalID1);
	}

	public Portal getPortal2()
	{
		return (Portal) aState.getPropFromID(portalID2);
	}

	public SolarSystem getSolarSystem1()
	{
		return aState.getSolarSystem(ssID1);
	}

	public SolarSystem getSolarSystem2()
	{
		return aState.getSolarSystem(ssID2);
	}

	public boolean isRecursive()
	{
		return ssID1 == ssID2;
	}

	public void moveShip(final Ship s)
	{
		aShipSet.put(s, aShipSet.get(s) + 1);
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

	public void setPropID1(final int id)
	{
		portalID1 = id;
	}

	public void setPropID2(final int id)
	{
		portalID2 = id;
	}

	@Override
	public Json toJson()
	{
		final List<Json> ships = new ArrayList<Json>(aShipSet.size());
		for (final Ship s : aShipSet.keySet()) {
			ships.add(new Json().setIntAttribute("progress", aShipSet.get(s)).setAttribute("ship", s));
		}
		final Json j = new Json();
		j.setIntAttribute("ss1", ssID1);
		j.setIntAttribute("ss2", ssID2);
		j.setIntAttribute("p1", portalID1);
		j.setIntAttribute("p2", portalID2);
		j.setIntAttribute("turns", aTurns);
		j.setListAttribute("ships", ships);
		j.setIntAttribute("id", aID);
		return j;
	}
}

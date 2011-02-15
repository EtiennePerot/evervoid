package com.evervoid.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.prop.Ship;

public class Wormhole implements EverVoidContainer<Ship>, Jsonable
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
	/**
	 * A Map for a Ship to it's progress along the wormhole
	 */
	private final Map<Ship, Integer> aShipSet = new HashMap<Ship, Integer>();
	/**
	 * The Point of the first solar system
	 */
	private final SolarSystem aSolarSystem1;
	/**
	 * The "destination" planet of the wormhole
	 */
	private final SolarSystem aSolarSystem2;
	/**
	 * The number of turns it takes to cross this wormhole
	 */
	private final int aTurns;

	protected Wormhole(final Json j, final EverVoidGameState state)
	{
		aSolarSystem1 = state.getSolarSystem(j.getIntAttribute("system1"));
		aSolarSystem2 = state.getSolarSystem(j.getIntAttribute("system2"));
		aTurns = j.getIntAttribute("turns");
		for (final Json wormship : j.getListAttribute("ships")) {
			aShipSet.put(new Ship(wormship.getAttribute("ship"), state), wormship.getIntAttribute("progress"));
		}
	}

	protected Wormhole(final SolarSystem ss1, final SolarSystem ss2, final EverVoidGameState state)
	{
		aSolarSystem1 = ss1;
		aSolarSystem2 = ss2;
		aTurns = MathUtils.clampInt(sMinimumTurns,
				(int) (state.getGalaxy().getSolarSystemDistance(ss1, ss2) * sDistanceToTurnMultiplier), sMaximumTurns);
	}

	@Override
	public boolean addElem(final Ship s)
	{
		if (aShipSet.containsKey(s)) {
			return false;
		}
		aShipSet.put(s, 0);
		return true;
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
		return (aSolarSystem1.equals(ss1) && aSolarSystem2.equals(ss2))
				|| (aSolarSystem1.equals(ss2) && aSolarSystem2.equals(ss1));
	}

	@Override
	public boolean containsElem(final Ship s)
	{
		return aShipSet.containsKey(s);
	}

	@Override
	public Iterator<Ship> getIterator()
	{
		// TODO Auto-generated method stub
		return aShipSet.keySet().iterator();
	}

	public SolarSystem getSolarSystem1()
	{
		return aSolarSystem1;
	}

	public SolarSystem getSolarSystem2()
	{
		return aSolarSystem2;
	}

	public void moveShip(final Ship s)
	{
		aShipSet.put(s, aShipSet.get(s) + 1);
	}

	@Override
	public void removeElem(final Ship s)
	{
		aShipSet.remove(s);
	}

	@Override
	public Json toJson()
	{
		final List<Json> ships = new ArrayList<Json>(aShipSet.size());
		for (final Ship s : aShipSet.keySet()) {
			ships.add(new Json().setIntAttribute("progress", aShipSet.get(s)).setAttribute("ship", s));
		}
		return new Json().setIntAttribute("system1", aSolarSystem1.getID()).setIntAttribute("system2", aSolarSystem2.getID())
				.setIntAttribute("turns", aTurns).setListAttribute("ships", ships);
	}
}

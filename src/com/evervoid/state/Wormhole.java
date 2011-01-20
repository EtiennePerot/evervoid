package com.evervoid.state;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Ship;

public class Wormhole implements EverVoidContainer<Ship>
{
	/**
	 * The "originator" planet of the wormhole
	 */
	final Planet aPlanetOne;
	/**
	 * The "destination" planet of the wormhole
	 */
	final Planet aPlanetTwo;
	/**
	 * A Map for a Ship to it's progress along the wormhole
	 */
	Map<Ship, Integer> aShipSet;
	/**
	 * The size of the wormhole, represents the number of turn it takes to cross it
	 */
	final int aSize;

	protected Wormhole(final Planet aPlanet1, final Planet aPlanet2, final int size)
	{
		aPlanetOne = aPlanet1;
		aPlanetTwo = aPlanet2;
		aSize = size;
		aShipSet = new HashMap<Ship, Integer>();
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

	public void moveShip(final Ship s)
	{
		aShipSet.put(s, aShipSet.get(s) + 1);
	}

	@Override
	public void removeElem(final Ship s)
	{
		aShipSet.remove(s);
	}
}

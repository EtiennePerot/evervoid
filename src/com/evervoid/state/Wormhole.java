package com.evervoid.state;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.evervoid.state.prop.Ship;

public class Wormhole implements EverVoidContainer<Ship>
{
	/**
	 * The number of turns to cross this wormhole will be computed by (distance between solar systems) * this multiplier
	 */
	private static final double sDistanceToTurnMultiplier = 0.25;
	/**
	 * The Point of the first solar system
	 */
	private final Point3D aPointOne;
	/**
	 * The "destination" planet of the wormhole
	 */
	private final Point3D aPointTwo;
	/**
	 * A Map for a Ship to it's progress along the wormhole
	 */
	private final Map<Ship, Integer> aShipSet;
	/**
	 * The number of turns it takes to cross this wormhole
	 */
	private final int aTurns;

	protected Wormhole(final Point3D pPoint1, final Point3D pPoint2)
	{
		aPointOne = pPoint1;
		aPointTwo = pPoint2;
		aTurns = (int) (pPoint1.distanceTo(pPoint2) * sDistanceToTurnMultiplier);
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

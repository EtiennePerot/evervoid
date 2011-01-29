package com.evervoid.state;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.evervoid.state.prop.Ship;

public class Wormhole implements EverVoidContainer<Ship>
{
	/**
	 * A Map for a Ship to it's progress along the wormhole
	 */
	private final Map<Ship, Integer> aShipSet;
	/**
	 * The size of the wormhole, represents the number of turn it takes to cross it
	 */
	final double aSize;
	/**
	 * The Point of the first solar system
	 */
	private final Point3D fPointOne;
	/**
	 * The "destination" planet of the wormhole
	 */
	private final Point3D fPointTwo;

	protected Wormhole(final Point3D pPoint1, final Point3D pPoint2)
	{
		fPointOne = pPoint1;
		fPointTwo = pPoint2;
		aSize = pPoint1.distanceTo(pPoint2);
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

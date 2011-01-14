package com.evervoid.engine.solar;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.evervoid.engine.EverVoidContainer;
import com.evervoid.engine.prop.Planet;
import com.evervoid.engine.prop.Prop;
import com.evervoid.engine.prop.Ship;
import com.jme3.math.FastMath;

public class SolarSystem implements EverVoidContainer<Prop>
{
	final Set<Prop> aPropSet;
	final int aSize;

	// TODO - revert to protected
	public SolarSystem(final int size)
	{
		aSize = size;
		aPropSet = new HashSet<Prop>();
		// TODO - test, remove
		for (int i = 0; i < 20; i++)
		{
			final Point loc = new Point(FastMath.rand.nextInt(aSize), FastMath.rand.nextInt(aSize));
			aPropSet.add(new Ship(null, loc));
		}
		for (int i = 0; i < 10; i++)
		{
			final Point loc = new Point(FastMath.rand.nextInt(aSize), FastMath.rand.nextInt(aSize));
			aPropSet.add(new Planet(null, loc));
		}
	}

	@Override
	public boolean addElem(final Prop p)
	{
		final Point loc = p.getLocation();
		if (loc.x > aSize || loc.y > aSize)
		{
			return false;
		}
		return aPropSet.add(p);
	}

	@Override
	public boolean containsElem(final Prop p)
	{
		return aPropSet.contains(p);
	}

	@Override
	public Iterator<Prop> getIterator()
	{
		return aPropSet.iterator();
	}

	@Override
	public void removeElem(final Prop p)
	{
		aPropSet.remove(p);
	}
}
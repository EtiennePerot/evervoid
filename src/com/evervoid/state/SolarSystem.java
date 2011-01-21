package com.evervoid.state;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.jme3.math.FastMath;

public class SolarSystem implements EverVoidContainer<Prop>
{
	final Set<Prop> aPropSet;
	final Dimension aSize;

	public SolarSystem(final Dimension size)
	{
		aSize = size;
		aPropSet = new HashSet<Prop>();
		// TODO - test, remove
		for (int i = 0; i < 20; i++) {
			final GridLocation loc = new GridLocation(FastMath.rand.nextInt(aSize.width), FastMath.rand.nextInt(aSize.height));
			aPropSet.add(new Ship(null, loc, "SCOUT"));
		}
		for (int i = 0; i < 10; i++) {
			final GridLocation loc = new GridLocation(FastMath.rand.nextInt(aSize.width), FastMath.rand.nextInt(aSize.height));
			aPropSet.add(new Planet(null, loc, "ORANGETHINGY"));
		}
	}

	public SolarSystem(final int width, final int height)
	{
		this(new Dimension(width, height));
	}

	@Override
	public boolean addElem(final Prop p)
	{
		final GridLocation loc = p.getLocation();
		if (!loc.fitsIn(aSize)) {
			return false;
		}
		return aPropSet.add(p);
	}

	@Override
	public boolean containsElem(final Prop p)
	{
		return aPropSet.contains(p);
	}

	public Dimension getDimension()
	{
		return aSize;
	}

	public int getHeight()
	{
		return aSize.getHeight();
	}

	@Override
	public Iterator<Prop> getIterator()
	{
		return aPropSet.iterator();
	}

	public int getWidth()
	{
		return aSize.getWidth();
	}

	@Override
	public void removeElem(final Prop p)
	{
		aPropSet.remove(p);
	}
}

package com.evervoid.state;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.evervoid.state.prop.Star;
import com.jme3.math.FastMath;

public class SolarSystem implements EverVoidContainer<Prop>
{
	public static SolarSystem createRandomSolarSystem()
	{
		// TODO - make not 48
		final SolarSystem tSolar = new SolarSystem(48);
		return tSolar;
	}

	private final Dimension aDimension;
	private final Set<Prop> aPropSet;
	private final Star aStar;

	/**
	 * Default constructor.
	 * 
	 * @param size
	 *            Dimension of the solar system to use.
	 */
	private SolarSystem(final Dimension size)
	{
		aDimension = size;
		aPropSet = new HashSet<Prop>();
		// TODO - test, remove
		for (int i = 0; i < 20; i++) {
			final GridLocation loc = new GridLocation(FastMath.rand.nextInt(aDimension.width),
					FastMath.rand.nextInt(aDimension.height));
			aPropSet.add(new Ship(null, loc, "SCOUT"));
		}
		for (int i = 0; i < 10; i++) {
			final GridLocation loc = new GridLocation(FastMath.rand.nextInt(aDimension.width),
					FastMath.rand.nextInt(aDimension.height));
			aPropSet.add(new Planet(null, loc, "ORANGETHINGY"));
		}
		aStar = new Star(null, new GridLocation(size.width / 2 - 2, size.height / 2 - 2, 4, 4));
	}

	/**
	 * Overloaded constructor creating a square solar system.
	 * 
	 * @param size
	 *            Size of a side of the square representing the limit of the solar system.
	 */
	protected SolarSystem(final int size)
	{
		this(new Dimension(size, size));
	}

	/**
	 * Overloaded constructor creating a solar system of a certain width and height.
	 * 
	 * @param width
	 *            Width of the solar system.
	 * @param height
	 *            Height of the solar system.
	 */
	protected SolarSystem(final int width, final int height)
	{
		this(new Dimension(width, height));
	}

	@Override
	public boolean addElem(final Prop p)
	{
		final GridLocation loc = p.getLocation();
		if (!loc.fitsIn(aDimension)) {
			return false;
		}
		return aPropSet.add(p);
	}

	@Override
	public boolean containsElem(final Prop p)
	{
		return aPropSet.contains(p);
	}

	/**
	 * @return The dimension for of the solar system.
	 */
	public Dimension getDimension()
	{
		return aDimension;
	}

	/**
	 * @return The height of the solar system.
	 */
	public int getHeight()
	{
		return aDimension.getHeight();
	}

	@Override
	public Iterator<Prop> getIterator()
	{
		return aPropSet.iterator();
	}

	public int getRadius()
	{
		return Math.max(getHeight(), getWidth());
	}

	/**
	 * @return A GridLocation where the sun is located.
	 */
	public GridLocation getSunLocation()
	{
		return aStar.getLocation();
	}

	/**
	 * @return The width of the solar system.
	 */
	public int getWidth()
	{
		return aDimension.getWidth();
	}

	@Override
	public void removeElem(final Prop p)
	{
		aPropSet.remove(p);
	}
}

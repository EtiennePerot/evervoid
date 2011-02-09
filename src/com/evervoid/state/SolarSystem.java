package com.evervoid.state;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.evervoid.state.prop.Star;
import com.jme3.math.FastMath;

public class SolarSystem implements EverVoidContainer<Prop>, Jsonable
{
	public static SolarSystem createRandomSolarSystem(final EverVoidGameState state)
	{
		// TODO: make not 64, 48
		final int width = MathUtils.getRandomIntBetween(32, 64);
		final int height = MathUtils.getRandomIntBetween(24, 48);
		final SolarSystem tSolar = new SolarSystem(new Dimension(width, height), state);
		for (int i = 0; i < 20; i++) {
			final GridLocation loc = new GridLocation(FastMath.rand.nextInt(width), FastMath.rand.nextInt(height));
			tSolar.addElem(new Ship(state.getRandomPlayer(), loc, "square_scout"));
		}
		for (int i = 0; i < 10; i++) {
			final GridLocation loc = new GridLocation(FastMath.rand.nextInt(width), FastMath.rand.nextInt(height));
			tSolar.addElem(new Planet(state.getRandomPlayer(), loc, state.getPlanetData("orange")));
		}
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
	 * @param state
	 *            Reference to the game state
	 */
	private SolarSystem(final Dimension size, final EverVoidGameState state)
	{
		aDimension = size;
		aPropSet = new HashSet<Prop>();
		aStar = new Star(new GridLocation(size.width / 2 - 2, size.height / 2 - 2, 4, 4));
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

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("dimension", aDimension).setListAttribute("props", aPropSet);
	}
}

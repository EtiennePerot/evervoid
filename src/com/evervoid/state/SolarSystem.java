package com.evervoid.state;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.gamedata.PlanetData;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.evervoid.state.prop.Star;
import com.jme3.math.FastMath;

public class SolarSystem implements EverVoidContainer<Prop>, Jsonable
{
	private final Dimension aDimension;
	private final int aID;
	private final Set<Prop> aPropSet = new HashSet<Prop>();
	private Star aStar;
	private final EverVoidGameState aState;

	/**
	 * Default constructor.
	 * 
	 * @param size
	 *            Dimension of the solar system to use.
	 * @param state
	 *            Reference to the game state
	 */
	SolarSystem(final Dimension size, final int id, final EverVoidGameState state)
	{
		aState = state;
		aID = id;
		aDimension = size;
		aStar = Star.randomStar(aDimension, state);
		addElem(aStar);
	}

	SolarSystem(final Json j, final EverVoidGameState state)
	{
		aDimension = Dimension.fromJson(j.getAttribute("dimension"));
		aID = j.getIntAttribute("id");
		aState = state;
		aStar = null;
		for (final Json p : j.getListAttribute("props")) {
			final Player owner = state.getPlayerByName(p.getStringAttribute("player"));
			if (p.getStringAttribute("proptype").equalsIgnoreCase("planet")) {
				addElem(new Planet(owner, GridLocation.fromJson(p.getAttribute("location")), state.getPlanetData(p
						.getStringAttribute("type"))));
			}
			else if (p.getStringAttribute("proptype").equalsIgnoreCase("ship")) {
				addElem(new Ship(owner, GridLocation.fromJson(p.getAttribute("location")), p.getStringAttribute("type")));
			}
			else if (p.getStringAttribute("proptype").equalsIgnoreCase("star")) {
				aStar = new Star(GridLocation.fromJson(p.getAttribute("location")), state, p.getStringAttribute("type"));
				addElem(aStar);
			}
		}
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

	public int getID()
	{
		return aID;
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
	 * @return The glow color of the sun
	 */
	public Color getSunGlowColor()
	{
		return aStar.getGlowColor();
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

	/**
	 * Randomly populates this solar system
	 */
	void populateRandomly()
	{
		// All your lolships are belong to us
		for (int i = 0; i < 20; i++) {
			final GridLocation loc = new GridLocation(FastMath.rand.nextInt(aDimension.width),
					FastMath.rand.nextInt(aDimension.height));
			addElem(new Ship(aState.getRandomPlayer(), loc, "scout"));
		}
		// No one expects the lolpanets inquisition
		for (int i = 0; i < 10; i++) {
			final GridLocation loc = new GridLocation(FastMath.rand.nextInt(aDimension.width),
					FastMath.rand.nextInt(aDimension.height));
			final PlanetData randomPlanet = aState.getPlanetData((String) MathUtils.getRandomElement(aState.getPlanetTypes()));
			addElem(new Planet(aState.getRandomPlayer(), loc, randomPlanet));
		}
	}

	@Override
	public void removeElem(final Prop p)
	{
		aPropSet.remove(p);
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("dimension", aDimension).setListAttribute("props", aPropSet).setIntAttribute("id", aID);
	}
}

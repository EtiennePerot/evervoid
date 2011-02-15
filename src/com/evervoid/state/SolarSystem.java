package com.evervoid.state;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.gamedata.PlanetData;
import com.evervoid.gamedata.RaceData;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.evervoid.state.prop.Star;

public class SolarSystem implements EverVoidContainer<Prop>, Jsonable
{
	private final Dimension aDimension;
	private final int aID;
	private final Point3D aPoint;
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
	SolarSystem(final Dimension size, final Point3D point, final EverVoidGameState state)
	{
		aState = state;
		aID = state.getNextSolarID();
		aDimension = size;
		aPoint = point;
		aStar = Star.randomStar(aDimension, state);
		addElem(aStar);
	}

	SolarSystem(final Json j, final EverVoidGameState state)
	{
		aDimension = Dimension.fromJson(j.getAttribute("dimension"));
		aPoint = Point3D.fromJson(j.getAttribute("point"));
		aID = j.getIntAttribute("id");
		aState = state;
		aStar = null;
		for (final Json p : j.getListAttribute("props")) {
			if (p.getStringAttribute("proptype").equalsIgnoreCase("planet")) {
				addElem(new Planet(p, state));
			}
			else if (p.getStringAttribute("proptype").equalsIgnoreCase("ship")) {
				addElem(new Ship(p, state));
			}
			else if (p.getStringAttribute("proptype").equalsIgnoreCase("star")) {
				aStar = new Star(p, state);
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

	/**
	 * @return The location of this solar system in 3D space
	 */
	public Point3D getPoint3D()
	{
		return aPoint;
	}

	/**
	 * Finds a prop at the given point
	 * 
	 * @param point
	 *            The point to look at
	 * @return The prop at the given point, or null if the point is free
	 */
	public Prop getPropAt(final Point point)
	{
		for (final Prop prop : aPropSet) {
			if (prop.getLocation().collides(point)) {
				return prop;
			}
		}
		return null;
	}

	public int getRadius()
	{
		return Math.max(getHeight(), getWidth());
	}

	/**
	 * Finds a random vacant GridLocation to put a prop in
	 * 
	 * @param dimension
	 *            The dimension of the prop to fit
	 * @return The random GridLocation
	 */
	public GridLocation getRandomLocation(final Dimension dimension)
	{
		GridLocation loc = null;
		while (loc == null || isOccupied(loc)) {
			loc = new GridLocation(MathUtils.getRandomIntBetween(0, aDimension.width), MathUtils.getRandomIntBetween(0,
					aDimension.height), dimension).constrain(aDimension);
		}
		return loc;
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
	 * Finds if there is one or more props at the given GridLocation
	 * 
	 * @param location
	 *            The location to look at
	 * @return True if there is one or more props at the given location
	 */
	public boolean isOccupied(final GridLocation location)
	{
		for (final Prop prop : aPropSet) {
			if (prop.getLocation().collides(location)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Randomly populates this solar system
	 */
	void populateRandomly()
	{
		// All your lolships are belong to us
		for (int i = 0; i < 20; i++) {
			final Player randomP = aState.getRandomPlayer();
			final RaceData race = randomP.getRaceData();
			final String shipType = (String) MathUtils.getRandomElement(race.getShipTypes());
			addElem(new Ship(randomP, getRandomLocation(race.getShipData(shipType).getDimension()), shipType, aState));
		}
		// No one expects the lolplanets inquisition
		for (int i = 0; i < 10; i++) {
			final PlanetData randomPlanet = aState.getPlanetData((String) MathUtils.getRandomElement(aState.getPlanetTypes()));
			addElem(new Planet(aState.getRandomPlayer(), getRandomLocation(randomPlanet.getDimension()),
					randomPlanet.getType(), aState));
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
		return new Json().setAttribute("dimension", aDimension).setListAttribute("props", aPropSet).setIntAttribute("id", aID)
				.setAttribute("point", aPoint);
	}
}

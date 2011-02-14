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
	 * Finds if there is one or more props at the given GridLocation
	 * 
	 * @param location
	 *            The location to look at
	 * @return True if there is one or more props at the given location
	 */
	public boolean findsPropsAt(final GridLocation location)
	{
		for (final Prop prop : aPropSet) {
			if (prop.getLocation().collides(location)) {
				return true;
			}
		}
		return false;
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
		while (loc == null || findsPropsAt(loc)) {
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
	 * Randomly populates this solar system
	 */
	void populateRandomly()
	{
		// All your lolships are belong to us
		for (int i = 0; i < 20; i++) {
			final Player randomP = aState.getRandomPlayer();
			final RaceData race = randomP.getRaceData();
			final String shipType = (String) MathUtils.getRandomElement(race.getShipTypes());
			addElem(new Ship(randomP, getRandomLocation(race.getShipData(shipType).getDimension()), shipType));
		}
		// No one expects the lolplanets inquisition
		for (int i = 0; i < 10; i++) {
			final PlanetData randomPlanet = aState.getPlanetData((String) MathUtils.getRandomElement(aState.getPlanetTypes()));
			addElem(new Planet(aState.getRandomPlayer(), getRandomLocation(randomPlanet.getDimension()), randomPlanet));
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

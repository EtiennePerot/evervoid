package com.evervoid.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.data.PlanetData;
import com.evervoid.state.data.RaceData;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.geometry.Point3D;
import com.evervoid.state.observers.SolarObserver;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.evervoid.state.prop.Star;

public class SolarSystem implements EVContainer<Prop>, Jsonable
{
	private final Dimension aDimension;
	private final Map<Point, Prop> aGrid = new HashMap<Point, Prop>();
	private final int aID;
	private final Set<SolarObserver> aObservableSet;
	private final Point3D aPoint;
	private final SortedSet<Prop> aProps = new TreeSet<Prop>();
	private Star aStar;
	private final EVGameState aState;

	/**
	 * Default constructor.
	 * 
	 * @param size
	 *            Dimension of the solar system to use.
	 * @param state
	 *            Reference to the game state
	 */
	SolarSystem(final Dimension size, final Point3D point, final EVGameState state)
	{
		aState = state;
		aID = state.getNextSolarID();
		aDimension = size;
		aPoint = point;
		aStar = Star.randomStar(aDimension, state);
		addElem(aStar);
		aObservableSet = new HashSet<SolarObserver>();
	}

	SolarSystem(final Json j, final EVGameState state)
	{
		aDimension = new Dimension(j.getAttribute("dimension"));
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
		aObservableSet = new HashSet<SolarObserver>();
	}

	@Override
	public boolean addElem(final Prop prop)
	{
		final GridLocation loc = prop.getLocation();
		if (!loc.fitsIn(aDimension)) {
			return false;
		}
		prop.enterSS(this);
		prop.placeInContainer(this);
		return aProps.add(prop);
	}

	@Override
	public boolean containsElem(final Prop p)
	{
		return aProps.contains(p);
	}

	public void deregisterObserver(final SolarObserver sObserver)
	{
		aObservableSet.remove(sObserver);
	}

	/**
	 * Removes a Prop's Point pointers from the grid
	 * 
	 * @param prop
	 *            The Prop to remove
	 */
	public void deregisterPropLocation(final Prop prop)
	{
		for (final Point p : prop.getLocation().getPoints()) {
			aGrid.put(p, null);
		}
	}

	/**
	 * @return The dimension for of the solar system.
	 */
	public Dimension getDimension()
	{
		return aDimension;
	}

	/**
	 * Finds at least one prop (if there is one) at the given GridLocation. Here for convenience; use getPropsAt for
	 * completeness
	 * 
	 * @param location
	 *            The location to search at
	 * @return One prop at the given GridLocation, if any
	 */
	public Prop getFirstPropAt(final GridLocation location)
	{
		for (final Point p : location.getPoints()) {
			final Prop match = getPropAt(p);
			if (match != null) {
				return match;
			}
		}
		return null;
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
	public Iterable<Prop> getIterable()
	{
		return aProps;
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
		return aGrid.get(point);
	}

	/**
	 * Finds the prop(s) at the given GridLocation
	 * 
	 * @param location
	 *            The location to search at
	 * @return The set of props at the given location
	 */
	public Set<Prop> getPropsAt(final GridLocation location)
	{
		final Set<Prop> props = new HashSet<Prop>();
		for (final Point p : location.getPoints()) {
			final Prop match = getPropAt(p);
			if (match != null) {
				props.add(match);
			}
		}
		return props;
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

	public Star getStar()
	{
		return aStar;
	}

	/**
	 * @return A GridLocation where the sun is located.
	 */
	public GridLocation getSunLocation()
	{
		return aStar.getLocation();
	}

	/**
	 * @return The shadow color of the sun
	 */
	public Color getSunShadowColor()
	{
		return aStar.getShadowColor();
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
		return getFirstPropAt(location) != null;
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

	public void registerObserver(final SolarObserver sObserver)
	{
		aObservableSet.add(sObserver);
	}

	/**
	 * Adds a Prop's Point pointers to the grid
	 * 
	 * @param prop
	 *            The Prop to add
	 */
	public void registerPropLocation(final Prop prop)
	{
		for (final Point p : prop.getLocation().getPoints()) {
			aGrid.put(p, prop);
		}
	}

	@Override
	public void removeElem(final Prop p)
	{
		p.leaveSS();
		aProps.remove(p);
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("dimension", aDimension).setListAttribute("props", aProps).setIntAttribute("id", aID)
				.setAttribute("point", aPoint);
	}
}

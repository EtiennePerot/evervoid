package com.evervoid.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
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
import com.evervoid.state.observers.ShipObserver;
import com.evervoid.state.observers.SolarObserver;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.evervoid.state.prop.Star;

public class SolarSystem implements EVContainer<Prop>, Jsonable, ShipObserver
{
	/**
	 * Randomly populates this solar system
	 */
	static SolarSystem randomSolarSystem(final int width, final int height, final Point3D origin, final EVGameState state)
	{
		final Dimension dim = new Dimension(width, height);
		final SolarSystem ss = new SolarSystem(state.getNextSolarID(), dim, origin, state.getRandomStar(dim));
		// All your lolships are belong to us
		for (int i = 0; i < 20; i++) {
			final Player randomP = state.getRandomPlayer();
			final RaceData race = randomP.getRaceData();
			final String shipType = (String) MathUtils.getRandomElement(race.getShipTypes());
			final Ship tempElem = new Ship(state.getNextPropID(), randomP, ss.getRandomLocation(race.getShipData(shipType)
					.getDimension()), shipType);
			ss.addElem(tempElem);
			state.registerProp(tempElem);
		}
		// No one expects the lolplanets inquisition
		for (int i = 0; i < 10; i++) {
			final PlanetData randomPlanet = state.getPlanetData((String) MathUtils.getRandomElement(state.getPlanetTypes()));
			final Planet tempElem = new Planet(state.getNextPropID(), state.getRandomPlayer(),
					ss.getRandomLocation(randomPlanet.getDimension()), randomPlanet.getType(), state);
			ss.addElem(tempElem);
			state.registerProp(tempElem);
		}
		return ss;
	}

	private final Dimension aDimension;
	private final Map<Point, Prop> aGrid = new HashMap<Point, Prop>();
	private final int aID;
	private final Set<SolarObserver> aObservableSet;
	private final Point3D aPoint;
	private final SortedSet<Prop> aProps = new TreeSet<Prop>();
	private Star aStar;

	/**
	 * Default constructor.
	 * 
	 * @param size
	 *            Dimension of the solar system to use.
	 * @param state
	 *            Reference to the game state
	 */
	SolarSystem(final int id, final Dimension size, final Point3D point, final Star star)
	{
		aObservableSet = new HashSet<SolarObserver>();
		aID = id;
		aDimension = size;
		aPoint = point;
		aStar = star;
		addElem(aStar);
	}

	SolarSystem(final Json j, final EVGameState state)
	{
		aObservableSet = new HashSet<SolarObserver>();
		aDimension = new Dimension(j.getAttribute("dimension"));
		aPoint = Point3D.fromJson(j.getAttribute("point"));
		aID = j.getIntAttribute("id");
		aStar = null;
		for (final Json p : j.getListAttribute("props")) {
			Prop prop = null;
			if (p.getStringAttribute("proptype").equalsIgnoreCase("planet")) {
				prop = new Planet(p, state.getPlayerByName(p.getStringAttribute("player")), state.getPlanetData(p
						.getStringAttribute("planettype")));
			}
			else if (p.getStringAttribute("proptype").equalsIgnoreCase("ship")) {
				prop = new Ship(p, state.getPlayerByName(p.getStringAttribute("player")));
			}
			else if (p.getStringAttribute("proptype").equalsIgnoreCase("star")) {
				aStar = new Star(p, state.getPlayerByName(p.getStringAttribute("player")), state.getStarData(p
						.getStringAttribute("startype")));
				prop = aStar;
			}
			if (prop != null) {
				addElem(prop);
				state.registerProp(prop);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addElem(final Prop prop)
	{
		final GridLocation loc = prop.getLocation();
		if (!loc.fitsIn(aDimension)) {
			return false;
		}
		for (final Point p : prop.getLocation().getPoints()) {
			aGrid.put(p, prop);
		}
		prop.aContainer = this;
		// prop.enterContainer(this);
		if (prop instanceof Ship) {
			for (final SolarObserver observer : aObservableSet) {
				observer.shipEntered((Ship) prop);
			}
		}
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

	@Override
	public Iterable<Prop> elemIterator()
	{
		return aProps;
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
		if (location == null) {
			return null;
		}
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

	@Override
	public int getID()
	{
		return aID;
	}

	/**
	 * Return all direct neighbors of the given gridPoint in which props of dimension size could fit.
	 * 
	 * @param gridLocation
	 *            The location around which we are finding the neighbors
	 * @param ofSize
	 *            The dimension of the neighbor locations we should be returning.
	 * @return a set containing all gridLocation's direct, unoccupied neighbors of dimension "size"
	 */
	public Set<GridLocation> getNeighbours(final GridLocation gridLocation, final Dimension size)
	{
		final HashSet<GridLocation> neighbourSet = new LinkedHashSet<GridLocation>();
		final int x = gridLocation.getX();
		final int y = gridLocation.getY();
		for (int i = x - size.width; i < x + gridLocation.getWidth() + 1; i++) {
			for (int j = y + 2; j > y - gridLocation.getHeight() - size.width + 1; j--) {
				if (i < 0 || j - size.height < 0 || i + size.width >= getWidth() || j >= getHeight()) {
					continue;
				}
				final GridLocation tempLoc = new GridLocation(i, j, size);
				if (!isOccupied(tempLoc)) {
					neighbourSet.add(tempLoc);
				}
			}
		}
		return neighbourSet;
	}

	public Set<GridLocation> getNeighbours(final Prop prop, final Dimension size)
	{
		return getNeighbours(prop.getLocation(), size);
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

	public void registerObserver(final SolarObserver sObserver)
	{
		aObservableSet.add(sObserver);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeElem(final Prop prop)
	{
		if (aProps.contains(prop)) {
			for (final Point p : prop.getLocation().getPoints()) {
				aGrid.remove(p);
			}
			aProps.remove(prop);
			if (prop instanceof Ship) {
				for (final SolarObserver observer : aObservableSet) {
					observer.shipLeft((Ship) prop);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void shipBombed(final GridLocation bombLocation)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void shipDestroyed(final Ship ship)
	{
		aProps.remove(ship.getLocation());
	}

	@Override
	public void shipJumped(final EVContainer<Prop> newContainer)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void shipMoved(final Ship ship, final GridLocation oldLocation, final List<GridLocation> path)
	{
		for (final Point p : oldLocation.getPoints()) {
			aGrid.remove(p);
		}
		for (final Point p : path.get(path.size() - 1).getPoints()) {
			aGrid.put(p, ship);
		}
	}

	@Override
	public void shipShot(final GridLocation shootLocation)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void shipTookDamage(final int damageAmount)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("dimension", aDimension).setListAttribute("props", aProps).setIntAttribute("id", aID)
				.setAttribute("point", aPoint);
	}
}

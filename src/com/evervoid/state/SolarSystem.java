package com.evervoid.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
import com.evervoid.state.prop.Portal;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.evervoid.state.prop.ShipPath;
import com.evervoid.state.prop.Star;
import com.evervoid.utils.EVContainer;
import com.evervoid.utils.MathUtils;

public class SolarSystem implements EVContainer<Prop>, Jsonable, ShipObserver
{
	/**
	 * The location of the SolarSytem within its Galaxy.
	 */
	private final Point3D aCenter;
	/**
	 * The dimension of the solar system grid.
	 */
	private final Dimension aDimension;
	/**
	 * Mapping from point to prop. A null value for a point signifies the point is empty.
	 */
	private final Map<Point, Prop> aGrid;
	/**
	 * The Solar System's unique identifier.
	 */
	private final int aID;
	/**
	 * Esthetic name for the solar system, to be displayed to user.
	 */
	private final String aName;
	/**
	 * All the objects observing this SolarSystem.
	 */
	private final Set<SolarObserver> aObservableSet;
	/**
	 * A set of all props located in this solar system.
	 */
	private final SortedSet<Prop> aProps;
	/**
	 * The central star in this SolarSystem.
	 */
	private Star aStar;
	/**
	 * The state to which this solar system belongs.
	 */
	private final EVGameState aState;

	/**
	 * Creates a new SolarSystem with the given parameters.
	 * 
	 * @param dim
	 *            The dimensions of this new SolarSystem.
	 * @param center
	 *            The center point of the SolarSystem within the Galaxy.
	 * @param star
	 *            The Star to be placed at the center of the SolarSystem.
	 * @param state
	 *            The state to which this SolarSystem will belong.
	 */
	SolarSystem(final Dimension dim, final Point3D center, final Star star, final EVGameState state)
	{
		aState = state;
		// prime for observers
		aObservableSet = new HashSet<SolarObserver>();
		// prime for props
		aGrid = new HashMap<Point, Prop>();
		aProps = new TreeSet<Prop>();
		// set attributes
		aID = state.getNextSolarID();
		aDimension = dim;
		aCenter = center;
		aStar = star;
		// register the star, else it won't show up
		state.registerProp(star, this);
		aName = EVGameState.getRandomSolarSystemName();
	}

	/**
	 * Creates a SolarSystem based on the contents of the Json passed.
	 * 
	 * @param j
	 *            The Json object containing the pertinent information.
	 * @param state
	 *            The state to which the SolarSystem will belong.
	 */
	SolarSystem(final Json j, final EVGameState state)
	{
		// set state first so that other functions may use it
		aState = state;
		// prime fro observers
		aObservableSet = new HashSet<SolarObserver>();
		// prime for props
		aGrid = new HashMap<Point, Prop>();
		aProps = new TreeSet<Prop>();
		// parse attributes
		aDimension = new Dimension(j.getAttribute("dimension"));
		aCenter = Point3D.fromJson(j.getAttribute("point"));
		aID = j.getIntAttribute("id");
		// TODO fetch the star
		aStar = null;
		aName = j.getStringAttribute("name");
	}

	@Override
	public boolean addElem(final Prop prop)
	{
		final GridLocation loc = prop.getLocation();
		if (aProps.contains(prop) || !loc.fitsIn(aDimension)) {
			return false;
		}
		aProps.add(prop);
		for (final Point p : prop.getLocation().getPoints()) {
			aGrid.put(p, prop);
		}
		prop.enterContainer(this);
		if (prop instanceof Ship) {
			for (final SolarObserver observer : aObservableSet) {
				observer.shipEntered(this, (Ship) prop);
			}
		}
		return true;
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

	@Override
	public boolean equals(final Object other)
	{
		if (other == null || other.getClass() != this.getClass()) {
			return false;
		}
		final SolarSystem o = (SolarSystem) other;
		return getID() == o.getID();
	}

	/**
	 * @return The dimension for of the solar system.
	 */
	public Dimension getDimension()
	{
		return aDimension;
	}

	/**
	 * @return The set of points comprising the edges of the SolarSystem.
	 */
	private Set<Point> getEdges()
	{
		final Set<Point> aSet = new HashSet<Point>();
		// bottom and top rows
		for (int i = 0; i < getWidth(); i++) {
			aSet.add(new Point(i, 0));
			aSet.add(new Point(i, getHeight() - 1));
		}
		// left and right column
		for (int i = 0; i < getHeight(); i++) {
			aSet.add(new Point(0, i));
			aSet.add(new Point(getWidth() - 1, i));
		}
		return aSet;
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
	 * Return all direct neighbors of the given gridPoint in which props of dimension size could fit and that are not currenlty
	 * unoccupied.
	 * 
	 * @param gridLocation
	 *            The location around which we are finding the neighbors
	 * @param ofSize
	 *            The dimension of the neighbor locations we should be returning.
	 * @return a set containing all gridLocation's direct, unoccupied neighbors of dimension "size"
	 */
	public Set<GridLocation> getFreeNeighbours(final GridLocation gridLocation, final Dimension size)
	{
		final HashSet<GridLocation> neighbourSet = new LinkedHashSet<GridLocation>();
		// start at the point's origin
		final int x = gridLocation.getX();
		final int y = gridLocation.getY();
		for (int i = x - size.width; i < x + gridLocation.getWidth() + 1; i++) {
			// origin is bottom left, for for x value to the left, we have to do leftmost - size.width
			// for neighbors to the right you just have to do rightmost + 1
			for (int j = y + gridLocation.getHeight(); j > y - gridLocation.getHeight() - size.width + 1; j--) {
				// Now for the y. The top element will be located at topmost + 1, since the origin will be glued
				// to the top of the origin. The bottom elements will have to be offset by size.width, with by 1
				// in order to compensate for the size of the point itself.
				if (i < 0 || j - size.height < 0 || i + size.width >= getWidth() || j >= getHeight()) {
					// The original point is too close to an edge, so the potential neighbor location
					// is off the grid; we don't want anything to do with it.
					continue;
				}
				// add the point only if it isn't already occupied
				final GridLocation tempLoc = new GridLocation(i, j, size);
				if (!isOccupied(tempLoc)) {
					neighbourSet.add(tempLoc);
				}
			}
		}
		return neighbourSet;
	}

	/**
	 * Finds all direct neighbors of the prop in which a prop of dimensions "size" could fit.
	 * 
	 * @param prop
	 *            The prop whose neighbors are being found.
	 * @param size
	 *            The desired size of the neighbor GridLocations.
	 * @return The set containing all n
	 */
	public Set<GridLocation> getFreeNeighbours(final Prop prop, final Dimension size)
	{
		return getFreeNeighbours(prop.getLocation(), size);
	}

	/**
	 * @return The height of the solar system.
	 */
	public int getHeight()
	{
		return aDimension.getHeight();
	}

	/**
	 * @return The "home owner" of this solar system, or the NullPlayer if no regular player owns this solar system
	 */
	public Player getHomePlayer()
	{
		for (final Player p : aState.getPlayers()) {
			if (equals(p.getHomeSolarSystem())) {
				return p;
			}
		}
		return aState.getNullPlayer();
	}

	@Override
	public int getID()
	{
		return aID;
	}

	/**
	 * @return The SolarSytem's Esthetic name.
	 */
	public String getName()
	{
		return aName;
	}

	/**
	 * @return The location of this solar system in 3D space
	 */
	public Point3D getPoint3D()
	{
		return aCenter;
	}

	/**
	 * @return The set of all portal props in this solar system
	 */
	public Set<Portal> getPortals()
	{
		final Set<Portal> portals = new HashSet<Portal>();
		for (final Prop p : aProps) {
			if (p instanceof Portal) {
				portals.add((Portal) p);
			}
		}
		return portals;
	}

	/**
	 * Returns a reference to the Portal prop to the given solar system
	 * 
	 * @param ss
	 *            The destination solar system
	 * @return The Portal prop going to this solar system, or null if there is no such Portal.
	 */
	Portal getPortalTo(final SolarSystem ss)
	{
		for (final Prop p : aProps) {
			if (p instanceof Portal && ((Portal) p).connects(ss)) {
				// The prop must be a portal, and connect to the correct solar system.
				return (Portal) p;
			}
		}
		return null;
	}

	/**
	 * @return A potential location for a wormhole, along the edge of this SolarSystem.
	 */
	public GridLocation getPotentialWormholeLocation()
	{
		GridLocation tempLocation;
		Point p;
		do {
			p = MathUtils.getRandomElement(getEdges());
			if (p.x == 0 || p.x == getWidth() - 1) {
				// origin is either left or riggt border, it is vertical.
				tempLocation = new GridLocation(p, Portal.sVertial);
			}
			else {
				// we're not vertical, so horizontal it is
				tempLocation = new GridLocation(p, Portal.sHorizontal);
			}
		}// The second clause makes sure we haven't picked a location that goes off the grid
		while (isOccupied(tempLocation) && tempLocation.fitsIn(new GridLocation(0, 0, aDimension)));
		return tempLocation;
	}

	/**
	 * @return The prop at the given point, or null if the point is free
	 */
	public Prop getPropAt(final Point point)
	{
		return aGrid.get(point);
	}

	/**
	 * @return The set of props occupying the given GridLocation.
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

	/**
	 * @return The radius of this SolarSystem, it is defined by the max of the width and height.
	 */
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
		return getRandomLocation(dimension, 0);
	}

	/**
	 * Finds a random vacant GridLocation to put a prop in
	 * 
	 * @param dimension
	 *            The dimension of the prop to fit
	 * @param margin
	 *            Safety margin to avoid
	 * @return The random GridLocation
	 */
	public GridLocation getRandomLocation(final Dimension dimension, final int margin)
	{
		GridLocation loc = null;
		while (loc == null || isOccupied(loc)) {
			loc = new GridLocation(MathUtils.getRandomIntBetween(margin, aDimension.width - margin),
					MathUtils.getRandomIntBetween(margin, aDimension.height - margin), dimension).constrain(aDimension);
		}
		return loc;
	}

	/**
	 * @return The central star of this SolarSystem.
	 */
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
	 * @return The shadow color of the sun.
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
	 * @return True if this SolarSystem is connected to the parameter SolarySystem by a Wormhole.
	 */
	public boolean isConnectedTo(final SolarSystem solarSystem)
	{
		for (final Portal p : getPortals()) {
			if (p.connects(solarSystem)) {
				// we have found the connection
				return true;
			}
		}
		// no connection found
		return false;
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
	 * Populates the SolarSystem with props based on the contents of the Json.
	 * 
	 * @param j
	 *            The Json object containing the pertinent information.
	 */
	void populate(final Json j)
	{
		for (final Json p : j.getListAttribute("props")) {
			Prop prop = null;
			if (p.getStringAttribute("proptype").equalsIgnoreCase("planet")) {
				prop = new Planet(p, aState);
			}
			else if (p.getStringAttribute("proptype").equalsIgnoreCase("ship")) {
				prop = new Ship(p, aState);
			}
			else if (p.getStringAttribute("proptype").equalsIgnoreCase("star")) {
				prop = new Star(p, aState);
				aStar = (Star) prop;
			}
			else if (p.getStringAttribute("proptype").equalsIgnoreCase("portal")) {
				prop = new Portal(p, aState);
			}
			if (prop != null) {
				aState.registerProp(prop, this);
			}
		}
	}

	/**
	 * Randomly populates this solar system
	 */
	void populateRandomly()
	{
		final Player owner = getHomePlayer();
		if (!owner.isNullPlayer()) {
			// All your lolships are belong to us
			final RaceData race = owner.getRaceData();
			for (int i = 0; i < 8; i++) {
				final String shipType = MathUtils.getRandomElement(race.getShipTypes());
				final Ship tempElem = new Ship(owner, this, getRandomLocation(race.getShipData(shipType).getDimension(), 2),
						shipType, aState);
				aState.registerProp(tempElem, this);
			}
		}
		// No one expects the lolplanets inquisition
		for (int i = 0; i < 3; i++) {
			final PlanetData randomPlanet = aState.getPlanetData(MathUtils.getRandomElement(aState.getPlanetTypes()));
			final Planet tempElem = new Planet(aState.getNextPropID(), owner,
					getRandomLocation(randomPlanet.getDimension(), 4), randomPlanet.getType(), aState);
			tempElem.populateInitialBuildings();
			aState.registerProp(tempElem, this);
		}
		// Sprinkle some extra serving of neutral planets
		for (int i = 0; i < 10; i++) {
			final PlanetData randomPlanet = aState.getPlanetData(MathUtils.getRandomElement(aState.getPlanetTypes()));
			final Planet tempElem = new Planet(aState.getNextPropID(), aState.getNullPlayer(), getRandomLocation(
					randomPlanet.getDimension(), 4), randomPlanet.getType(), aState);
			aState.registerProp(tempElem, this);
		}
		// your order is ready
	}

	public void registerObserver(final SolarObserver sObserver)
	{
		aObservableSet.add(sObserver);
	}

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
					observer.shipLeft(this, (Ship) prop);
				}
				((Ship) prop).deregisterObserver(this);
			}
			return true;
		}
		return false;
	}

	@Override
	public void shipBombed(final Ship ship, final GridLocation bombLocation)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void shipCapturedPlanet(final Ship ship, final Planet planet, final ShipPath underlyingPath)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void shipDestroyed(final Ship ship)
	{
		for (final Point p : ship.getLocation().getPoints()) {
			aGrid.remove(p);
		}
		aProps.remove(ship);
	}

	@Override
	public void shipEnteredCargo(final Ship container, final Ship docker, final ShipPath shipPath)
	{
	}

	@Override
	public void shipEnteredThis(final Ship containerShip, final Ship ship)
	{
		// Do nothing
	}

	@Override
	public void shipExitedCargo(final Ship container, final Ship docker)
	{
		addElem(docker);
	}

	@Override
	public void shipHealthChanged(final Ship ship, final int health)
	{
	}

	@Override
	public void shipJumped(final Ship ship, final EVContainer<Prop> oldContainer, final ShipPath leavingMove,
			final EVContainer<Prop> newContainer)
	{
	}

	@Override
	public void shipLeftContainer(final Ship ship, final EVContainer<Prop> container, final ShipPath exitPath)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void shipMoved(final Ship ship, final GridLocation oldLocation, final ShipPath path)
	{
		for (final Point p : oldLocation.getPoints()) {
			aGrid.remove(p);
		}
		final List<GridLocation> elbows = path.getPath();
		for (final Point p : elbows.get(elbows.size() - 1).getPoints()) {
			aGrid.put(p, ship);
		}
	}

	@Override
	public void shipShieldsChanged(final Ship ship, final int shields)
	{
		// Nothing
	}

	@Override
	public void shipShot(final Ship ship, final GridLocation shootLocation)
	{
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("dimension", aDimension).setListAttribute("props", aProps).setAttribute("id", aID)
				.setAttribute("point", aCenter).setAttribute("name", aName);
	}
}

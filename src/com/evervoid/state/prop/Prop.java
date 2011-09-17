package com.evervoid.state.prop;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.player.Player;
import com.evervoid.utils.EVContainer;

/**
 * Props represent all objects in the game. They must be owned by a Player (by default the neutral Player) and have a dimension
 * that they occupy in their parent {@link SolarSystem}. Props must at all times be contained by an {@link EVContainer} Prop
 * container.
 */
public abstract class Prop implements Jsonable, Comparable<Prop>
{
	/**
	 * The Container to which this prop belongs.
	 */
	protected EVContainer<Prop> aContainer = null;
	/**
	 * The ID with which this Prop has been registered to the State.
	 */
	protected final int aID;
	/**
	 * The location of this Prop within the container if it applies.
	 */
	protected GridLocation aLocation;
	/**
	 * This Prop's owner.
	 */
	protected Player aOwner;
	/**
	 * The String identifier of this Prop's type.
	 */
	protected final String aPropType;
	/**
	 * The State to which this Prop belongs.
	 */
	protected final EVGameState aState;

	/**
	 * Constructs a Prop from the given parameters.
	 * 
	 * @param id
	 *            The id that will be given to this Prop.
	 * @param owner
	 *            This Props owner.
	 * @param location
	 *            The location this Prop will occupy in the Container if it an instance of SolarSystem.
	 * @param propType
	 *            The String representation of this Prop's type.
	 * @param state
	 *            The state this Prop will belong to.
	 */
	protected Prop(final int id, final Player owner, final GridLocation location, final String propType, final EVGameState state)
	{
		aState = state;
		aOwner = owner;
		aLocation = location.clone();
		aID = id;
		aPropType = propType;
	}

	/**
	 * Creates a prop from the contents of the Json and belonging to the passed state.
	 * 
	 * @param j
	 *            The Json from which to build the Prop.
	 * @param state
	 *            The State to which this Prop will belong.
	 */
	protected Prop(final Json j, final EVGameState state)
	{
		// get the relevant data and pass it to the actual constructor
		this(j.getIntAttribute("id"), state.getPlayerByName(j.getStringAttribute("player")), new GridLocation(
				j.getAttribute("location")), j.getStringAttribute("proptype"), state);
		aContainer = state.getSolarSystem(j.getIntAttribute("container"));
	}

	@Override
	public int compareTo(final Prop other)
	{
		return getID() - other.getID();
	}

	/**
	 * Deregisters this Prop's location to the Container, if any
	 */
	public void deregister()
	{
		if (aContainer == null) {
			return;
		}
		aContainer.removeElem(this);
		aContainer = null;
	}

	/**
	 * Call this when the prop enters a container or is created in a container
	 * 
	 * @param container
	 *            The solar system that the prop is in
	 * @return Whether the container was entered successfully
	 */
	public boolean enterContainer(final EVContainer<Prop> container)
	{
		if (container != null && container.addElem(this)) {
			aContainer = container;
			return true;
		}
		// failed to enter container
		return false;
	}

	@Override
	public boolean equals(final Object other)
	{
		if (other == null || !(other instanceof Prop)) {
			return false;
		}
		final Prop o = (Prop) other;
		return o.getID() == getID();
	}

	/**
	 * @return The Container that this Prop is in, or none if unattached
	 */
	public EVContainer<Prop> getContainer()
	{
		return aContainer;
	}

	/**
	 * @return The dimension of this Prop.
	 */
	public Dimension getDimension()
	{
		return aLocation.dimension.clone();
	}

	/**
	 * @param dimension
	 *            The dimension of the neighbors for which to look
	 * @return The origins of the set of all empty neighboring points in which elements of size dimension could fit.
	 */
	public Set<Point> getFreeNeighborOrigins(final Dimension dimension)
	{
		final Set<Point> set = new HashSet<Point>();
		if (aContainer instanceof SolarSystem) {
			for (final GridLocation loc : ((SolarSystem) aContainer).getFreeNeighbours(getLocation(), dimension)) {
				set.add(loc.origin);
			}
		}
		return set;
	}

	/**
	 * @param dimension
	 *            the dimensions of the neighbors for which to look
	 * @return the set of all empty neighboring points in which elements of size dimension could fit.
	 */
	public Set<GridLocation> getFreeNeighbors(final Dimension dimension)
	{
		if (aContainer instanceof SolarSystem) {
			return ((SolarSystem) aContainer).getFreeNeighbours(getLocation(), dimension);
		}
		return null;
	}

	/**
	 * @return This Prop's height.
	 */
	public int getHeight()
	{
		return aLocation.getHeight();
	}

	/**
	 * @return The ID with which this Prop is registered to its state.
	 */
	public int getID()
	{
		return aID;
	}

	/**
	 * @return This Prop's location within its parent SolarSystem.
	 */
	public GridLocation getLocation()
	{
		return aLocation.clone();
	}

	/**
	 * @param dimension
	 *            the dimensions of the neighbors for which to look
	 * @return the set of all neighboring points in which elements of size dimension could fit.
	 */
	public Set<GridLocation> getNeighbors(final Dimension dimension)
	{
		if (aContainer instanceof SolarSystem) {
			return ((SolarSystem) aContainer).getNeighbours(getLocation(), dimension);
		}
		return null;
	}

	/**
	 * @return This Prop's owner.
	 */
	public Player getPlayer()
	{
		return aOwner;
	}

	/**
	 * @return The String representation of this Prop's type.
	 */
	public String getPropType()
	{
		return aPropType;
	}

	/**
	 * @return The State to which this Prop belongs.
	 */
	public EVGameState getState()
	{
		return aState;
	}

	/**
	 * @return The width of this Prop's dimension.
	 */
	public int getWidth()
	{
		return aLocation.getWidth();
	}

	/**
	 * @return The x coordinate of this Prop's origin.
	 */
	public int getX()
	{
		return aLocation.getX();
	}

	/**
	 * @return The y coordinate of this Prop's origin.
	 */
	public int getY()
	{
		return aLocation.getY();
	}

	/**
	 * @return Whether this Prop should be ignored by the pathfinder or not
	 */
	public boolean ignorePathfinder()
	{
		return false;
	}

	/**
	 * @param location
	 *            The location to check.
	 * @return Whether this prop is a neighbor of the given location.
	 */
	public boolean isNeighborOf(final GridLocation location)
	{
		return getNeighbors(location.getDimension()).contains(location);
	}

	/**
	 * @param other
	 *            The prop
	 * @return Whether the other Prop is a neighbor of this Prop.
	 */
	public boolean isNeighborOf(final Prop other)
	{
		return isNeighborOf(other.getLocation());
	}

	/**
	 * Call this when the prop leaves the container (or gets destroyed, or goes inside a carrier ship...)
	 */
	public void leaveContainer()
	{
		deregister();
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setAttribute("player", aOwner.getName());
		j.setAttribute("location", aLocation);
		j.setAttribute("id", aID);
		j.setAttribute("proptype", getPropType());
		j.setAttribute("container", aContainer.getID());
		return j;
	}

	@Override
	public String toString()
	{
		return "Prop_" + aPropType + " ID " + getID() + " of " + aOwner + " at " + aLocation;
	}
}

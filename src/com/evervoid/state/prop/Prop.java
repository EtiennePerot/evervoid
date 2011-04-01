package com.evervoid.state.prop;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVContainer;
import com.evervoid.state.EVGameState;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.player.Player;

public abstract class Prop implements Jsonable, Comparable<Prop>
{
	protected EVContainer<Prop> aContainer = null;
	protected final int aID;
	protected GridLocation aLocation;
	protected Player aPlayer;
	protected final String aPropType;
	protected final EVGameState aState;

	protected Prop(final int id, final Player player, final GridLocation location, final String propType,
			final EVGameState state)
	{
		aState = state;
		aPlayer = player;
		aLocation = location.clone();
		aID = id;
		aPropType = propType;
	}

	protected Prop(final Json j, final String propType, final EVGameState state)
	{
		// get the relevant data and pass it to the actual constructor
		this(j.getIntAttribute("id"), state.getPlayerByName(j.getStringAttribute("player")), new GridLocation(
				j.getAttribute("location")), propType, state);
		aContainer = state.getSolarSystem(j.getIntAttribute("container"));
		aState.registerProp(this);
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
	 */
	public boolean enterContainer(final EVContainer<Prop> container)
	{
		if (container != aContainer && register()) {
			aContainer = container;
		}
		return false;
	}

	@Override
	public boolean equals(final Object other)
	{
		if (other == null || other.getClass() != this.getClass()) {
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

	public Dimension getDimension()
	{
		return aLocation.dimension.clone();
	}

	public int getHeight()
	{
		return aLocation.getHeight();
	}

	public int getID()
	{
		return aID;
	}

	public GridLocation getLocation()
	{
		return aLocation.clone();
	}

	public Player getPlayer()
	{
		return aPlayer;
	}

	public String getPropType()
	{
		return aPropType;
	}

	public EVGameState getState()
	{
		return aState;
	}

	public int getWidth()
	{
		return aLocation.getWidth();
	}

	public int getX()
	{
		return aLocation.getX();
	}

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
	 * Call this when the prop leaves the container (or gets destroyed, or goes inside a carrier ship...)
	 */
	public void leaveContainer()
	{
		deregister();
	}

	/**
	 * Registers this Prop's location to the Solar System, if any
	 */
	public boolean register()
	{
		if (aContainer == null) {
			return false;
		}
		return aContainer.addElem(this);
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setStringAttribute("player", aPlayer.getName());
		j.setAttribute("location", aLocation);
		j.setIntAttribute("id", aID);
		j.setStringAttribute("proptype", getPropType());
		j.setIntAttribute("container", aContainer.getID());
		return j;
	}

	@Override
	public String toString()
	{
		return "Prop_" + aPropType + " of " + aPlayer + " at " + aLocation;
	}
}

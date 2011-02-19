package com.evervoid.state.prop;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVContainer;
import com.evervoid.state.EVGameState;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.player.Player;

public abstract class Prop implements Jsonable, Comparable<Prop>
{
	protected EVContainer<Prop> aContainer = null;
	private final int aID;
	protected GridLocation aLocation;
	protected final Player aPlayer;
	private final String aPropType;

	protected Prop(final Json j, final EVGameState state, final String propType)
	{
		// get the relevant data and pass it to the actual constructor
		this(state.getPlayerByName(j.getStringAttribute("player")), new GridLocation(j.getAttribute("location")), state,
				propType);
	}

	protected Prop(final Player player, final GridLocation location, final EVGameState state, final String propType)
	{
		if (player == null) {
			aPlayer = state.getNullPlayer();
		}
		else {
			aPlayer = player;
		}
		aLocation = (GridLocation) location.clone();
		aID = state.getNextPropID();
		aPropType = propType;
		state.registerProp(this);
	}

	protected Json basePropJson()
	{
		return new Json().setStringAttribute("player", aPlayer.getName()).setAttribute("location", aLocation)
				.setIntAttribute("id", aID).setStringAttribute("proptype", getPropType());
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
	}

	/**
	 * Call this when the prop enters a container or is created in a container
	 * 
	 * @param container
	 *            The solar system that the prop is in
	 */
	public void enterContainer(final EVContainer<Prop> container)
	{
		if (container == aContainer) {
			return;
		}
		deregister();
		aContainer = container;
		register();
	}

	/**
	 * @return The Container that this Prop is in, or none if unattached
	 */
	public EVContainer<Prop> getContainer()
	{
		return aContainer;
	}

	public int getID()
	{
		return aID;
	}

	public GridLocation getLocation()
	{
		return (GridLocation) aLocation.clone();
	}

	public Player getPlayer()
	{
		return aPlayer;
	}

	public String getPropType()
	{
		return aPropType;
	}

	/**
	 * Call this when the prop leaves the container (or gets destroyed, or goes inside a carrier ship...)
	 */
	public void leaveContainer()
	{
		deregister();
		aContainer = null;
	}

	/**
	 * Registers this Prop's location to the Solar System, if any
	 */
	public void register()
	{
		if (aContainer == null) {
			return;
		}
		aContainer.addElem(this);
	}

	@Override
	public abstract Json toJson();

	@Override
	public String toString()
	{
		return "Prop_" + aPropType + " of " + aPlayer + " at " + aLocation;
	}
}

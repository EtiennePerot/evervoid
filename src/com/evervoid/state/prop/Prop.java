package com.evervoid.state.prop;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVContainer;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.player.Player;

public abstract class Prop implements Jsonable, Comparable<Prop>
{
	protected EVContainer<Prop> aContainer;
	private final int aID;
	protected GridLocation aLocation;
	protected final Player aPlayer;
	private final String aPropType;
	private SolarSystem aSolarSystem = null;

	protected Prop(final Json j, final EVGameState state, final String propType)
	{
		// get the relevant data and pass it to the actual constructor
		this(state.getPlayerByName(j.getStringAttribute("player")), new GridLocation(j.getAttribute("location")), state,
				propType);
	}

	protected Prop(final Player player, final GridLocation location, final EVGameState state, final String propType)
	{
		aPlayer = player;
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
	 * Deregisters this Prop's location to the Solar System, if any
	 */
	private void deregisterFromSS()
	{
		if (aSolarSystem == null) {
			return;
		}
		aSolarSystem.deregisterPropLocation(this);
	}

	/**
	 * Call this when the prop enters a solar system or is created in a solar system
	 * 
	 * @param ss
	 *            The solar system that the prop is in
	 */
	public void enterSS(final SolarSystem ss)
	{
		if (ss == aSolarSystem) {
			return;
		}
		deregisterFromSS();
		aSolarSystem = ss;
		registerToSS();
	}

	public int getID()
	{
		return aID;
	}

	public GridLocation getLocation()
	{
		return (GridLocation) aLocation.clone();
	}

	public String getPropType()
	{
		return aPropType;
	}

	/**
	 * Call this when the prop leave the solar system grid (or gets destroyed, or goes inside a carrier ship...)
	 */
	public void leaveSS()
	{
		deregisterFromSS();
		aSolarSystem = null;
	}

	/**
	 * Call this in subclasses when the prop needs to move to a new location
	 * 
	 * @param location
	 *            The location to move to
	 */
	protected void move(final GridLocation location)
	{
		deregisterFromSS();
		aLocation = (GridLocation) location.clone();
		registerToSS();
	}

	public void placeInContainer(final EVContainer<Prop> container)
	{
		aContainer = container;
	}

	/**
	 * Registers this Prop's location to the Solar System, if any
	 */
	private void registerToSS()
	{
		if (aSolarSystem == null) {
			return;
		}
		aSolarSystem.registerPropLocation(this);
	}

	@Override
	public abstract Json toJson();

	@Override
	public String toString()
	{
		return "Prop_" + aPropType + " of " + aPlayer + "at " + aLocation;
	}
}

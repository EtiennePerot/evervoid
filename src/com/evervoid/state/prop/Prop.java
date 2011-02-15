package com.evervoid.state.prop;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EverVoidGameState;
import com.evervoid.state.GridLocation;
import com.evervoid.state.player.Player;

public abstract class Prop implements Jsonable
{
	private final int aID;
	protected GridLocation aLocation;
	protected final Player aPlayer;
	private final String aPropType;

	protected Prop(final Json j, final EverVoidGameState state, final String propType)
	{
		// get the relevant data and pass it to the actual constructor
		this(state.getPlayerByName(j.getStringAttribute("player")), GridLocation.fromJson(j.getAttribute("location")), state,
				propType);
	}

	protected Prop(final Player player, final GridLocation location, final EverVoidGameState state, final String propType)
	{
		aPlayer = player;
		aLocation = location;
		aID = state.getNextPropID();
		aPropType = propType;
		state.registerProp(this);
	}

	public int getID()
	{
		return aID;
	}

	public GridLocation getLocation()
	{
		return aLocation;
	}

	public String getPropType()
	{
		return aPropType;
	}

	void move(final GridLocation location)
	{
		aLocation = location;
	}

	@Override
	public Json toJson()
	{
		// TODO - this should be an abstract function with no implementation
		return new Json().setStringAttribute("player", aPlayer.getName()).setAttribute("location", aLocation)
				.setIntAttribute("id", aID).setStringAttribute("proptype", getPropType());
	}
}

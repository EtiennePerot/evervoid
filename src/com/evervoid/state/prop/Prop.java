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

	protected Prop(final Json j, final EverVoidGameState state)
	{
		aPlayer = state.getPlayerByName(j.getStringAttribute("player"));
		aLocation = GridLocation.fromJson(j.getAttribute("location"));
		aID = j.getIntAttribute("id");
		state.registerProp(this);
	}

	protected Prop(final Player player, final GridLocation location, final EverVoidGameState state)
	{
		aPlayer = player;
		aLocation = location;
		aID = state.getPropID();
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

	public abstract String getPropType();

	void move(final GridLocation location)
	{
		aLocation = location;
	}

	@Override
	public Json toJson()
	{
		return new Json().setStringAttribute("player", aPlayer.getName()).setAttribute("location", aLocation)
				.setIntAttribute("id", aID).setStringAttribute("proptype", getPropType());
	}
}

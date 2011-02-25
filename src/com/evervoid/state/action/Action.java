package com.evervoid.state.action;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVGameState;
import com.evervoid.state.player.Player;

public abstract class Action implements Jsonable
{
	protected final String aActionType;
	protected final Player aPlayer;

	public Action(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		this(state.getPlayerByName(j.getStringAttribute("player")), j.getStringAttribute("actiontype"));
	}

	public Action(final Player player, final String actionType) throws IllegalEVActionException
	{
		aPlayer = player;
		aActionType = actionType;
	}

	public abstract void execute();

	public String getActionType()
	{
		return aActionType;
	}

	public abstract boolean isValid();

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setStringAttribute("player", aPlayer.getName());
		j.setStringAttribute("actiontype", aActionType);
		return j;
	}
}

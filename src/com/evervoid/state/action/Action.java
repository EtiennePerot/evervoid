package com.evervoid.state.action;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVGameState;
import com.evervoid.state.player.Player;

public abstract class Action implements Jsonable
{
	protected final String aActionType;
	protected final Player aPlayer;
	protected final EVGameState aState;

	public Action(final Json j, final EVGameState state)
	{
		this(j.getStringAttribute("player"), j.getStringAttribute("actiontype"), state);
	}

	public Action(final String playerName, final String actionType, final EVGameState state)
	{
		aState = state;
		aPlayer = aState.getPlayerByName(playerName);
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

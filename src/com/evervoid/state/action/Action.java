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
		aState = state;
		aPlayer = aState.getPlayerByName(j.getStringAttribute("player"));
		aActionType = j.getStringAttribute("actiontype");
	}

	public Action(final Player player, final String actionType, final EVGameState state)
	{
		aState = state;
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
		return new Json().setStringAttribute("player", aPlayer.getName()).setStringAttribute("actiontype", getActionType());
	}
}

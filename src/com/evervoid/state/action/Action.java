package com.evervoid.state.action;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.player.Player;

public abstract class Action implements Jsonable
{
	private final String aPlayerName;

	public Action(final Player player)
	{
		aPlayerName = player.getName();
	}

	public abstract String getActionType();

	@Override
	public Json toJson()
	{
		return new Json().setStringAttribute("player", aPlayerName).setStringAttribute("actiontype", getActionType());
	}
}

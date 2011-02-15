package com.evervoid.state.action;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.player.Player;

public abstract class Action implements Jsonable
{
	private final String aActionType;
	private final String aPlayerName;

	public Action(final Player player, final String actionType)
	{
		aPlayerName = player.getName();
		aActionType = actionType;
	}

	public String getActionType()
	{
		return aActionType;
	}

	@Override
	public Json toJson()
	{
		return new Json().setStringAttribute("player", aPlayerName).setStringAttribute("actiontype", getActionType());
	}
}

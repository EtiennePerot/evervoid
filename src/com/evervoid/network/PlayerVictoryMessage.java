package com.evervoid.network;

import com.evervoid.json.Json;
import com.evervoid.state.player.Player;

public class PlayerVictoryMessage extends EverMessage
{
	public PlayerVictoryMessage(final Player victor)
	{
		super(new Json(victor.getName()), PlayerVictoryMessage.class.getName());
	}
}

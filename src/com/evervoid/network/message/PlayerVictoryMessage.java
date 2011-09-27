package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;
import com.evervoid.state.player.Player;

public class PlayerVictoryMessage extends EVMessage
{
	public PlayerVictoryMessage(final Player victor)
	{
		super(new Json(victor.getName()));
	}
}

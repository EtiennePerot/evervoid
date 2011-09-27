package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;
import com.evervoid.state.player.Player;

public class PlayerDefeatedMessage extends EVMessage
{
	public PlayerDefeatedMessage(final Player defeated)
	{
		super(new Json(defeated.getName()));
	}
}

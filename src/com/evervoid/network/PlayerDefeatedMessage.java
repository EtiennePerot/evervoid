package com.evervoid.network;

import com.evervoid.json.Json;
import com.evervoid.state.player.Player;

public class PlayerDefeatedMessage extends EverMessage
{
	public PlayerDefeatedMessage(final Player defeated)
	{
		super(new Json(defeated.getName()));
	}
}

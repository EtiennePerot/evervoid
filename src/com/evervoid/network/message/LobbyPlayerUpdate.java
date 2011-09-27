package com.evervoid.network.message;

import com.evervoid.network.EVMessage;
import com.evervoid.network.lobby.LobbyPlayer;

public class LobbyPlayerUpdate extends EVMessage
{
	public LobbyPlayerUpdate(final LobbyPlayer player)
	{
		super(player);
	}
}

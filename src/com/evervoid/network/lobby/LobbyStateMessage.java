package com.evervoid.network.lobby;

import com.evervoid.network.EVMessage;

public class LobbyStateMessage extends EVMessage
{
	public LobbyStateMessage(final LobbyState lobbyState, final LobbyPlayer player)
	{
		super(lobbyState.getJsonForPlayer(player));
	}
}

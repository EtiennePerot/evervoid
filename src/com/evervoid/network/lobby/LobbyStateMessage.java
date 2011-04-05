package com.evervoid.network.lobby;

import com.evervoid.network.EverMessage;

public class LobbyStateMessage extends EverMessage
{
	public LobbyStateMessage(final LobbyState lobbyState, final LobbyPlayer player)
	{
		super(lobbyState.getJsonForPlayer(player));
	}
}

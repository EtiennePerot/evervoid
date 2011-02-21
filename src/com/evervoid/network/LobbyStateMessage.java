package com.evervoid.network;

import com.evervoid.server.LobbyState;

public class LobbyStateMessage extends EverMessage
{
	public LobbyStateMessage(final LobbyState lobbyState)
	{
		super(lobbyState, "lobbydata");
	}
}

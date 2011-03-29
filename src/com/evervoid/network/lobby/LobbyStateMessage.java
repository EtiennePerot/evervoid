package com.evervoid.network.lobby;

import com.evervoid.network.EverMessage;

public class LobbyStateMessage extends EverMessage
{
	public LobbyStateMessage(final LobbyState lobbyState)
	{
		super(lobbyState, LobbyStateMessage.class.getName());
	}
}

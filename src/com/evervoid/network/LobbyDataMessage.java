package com.evervoid.network;

import com.evervoid.state.EVGameState;

public class LobbyDataMessage extends EverMessage
{
	public LobbyDataMessage(final EVGameState state)
	{
		super(state.getPlayers(), "lobbydata");
	}
}

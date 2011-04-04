package com.evervoid.network.lobby;

import com.evervoid.json.Json;
import com.evervoid.network.EverMessage;

public class LobbyStateMessage extends EverMessage
{
	private final String aClientName;

	public LobbyStateMessage(final LobbyState lobbyState, final String clientName)
	{
		// hack, but we don't use reflection yet...
		super(lobbyState.toJson().setStringAttribute("clientName", clientName));
		aClientName = clientName;
	}

	@Override
	public Json getJson()
	{
		return super.getJson().setStringAttribute("clientName", aClientName);
	}
}

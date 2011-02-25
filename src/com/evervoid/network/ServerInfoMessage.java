package com.evervoid.network;

import com.evervoid.json.Json;
import com.evervoid.network.lobby.LobbyState;

/**
 * Sent by the server, this message contains general server info
 */
public class ServerInfoMessage extends EverMessage
{
	public ServerInfoMessage(final LobbyState lobby, final boolean inGame)
	{
		super(new Json().setBooleanAttribute("ingame", inGame).setIntAttribute("players", lobby.getNumOfPlayers())
				.setStringAttribute("name", lobby.getServerName()), "serverinfo");
	}
}

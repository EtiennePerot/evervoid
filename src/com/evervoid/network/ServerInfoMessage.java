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
		super(new Json().setAttribute("ingame", inGame).setAttribute("players", lobby.getNumOfPlayers())
				.setAttribute("name", lobby.getServerName()));
	}
}

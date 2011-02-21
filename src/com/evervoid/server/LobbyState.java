package com.evervoid.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Color;
import com.evervoid.state.data.GameData;
import com.jme3.network.connection.Client;

public class LobbyState implements Jsonable, Iterable<LobbyPlayer>
{
	private final GameData aGameData;
	private final List<LobbyPlayer> aLobbyPlayers = new ArrayList<LobbyPlayer>();

	LobbyState(final GameData gamedata)
	{
		aGameData = gamedata;
	}

	LobbyPlayer addPlayer(final Client client, final String nickname)
	{
		final LobbyPlayer newPlayer = new LobbyPlayer(client, nickname, aGameData.getRandomRace(), Color.random());
		aLobbyPlayers.add(newPlayer);
		return newPlayer;
	}

	LobbyPlayer getPlayerByClient(final Client client)
	{
		for (final LobbyPlayer player : aLobbyPlayers) {
			if (player.getClient().equals(client)) {
				return player;
			}
		}
		return null;
	}

	@Override
	public Iterator<LobbyPlayer> iterator()
	{
		return aLobbyPlayers.iterator();
	}

	void removePlayer(final Client client)
	{
		if (getPlayerByClient(client) != null) {
			removePlayer(getPlayerByClient(client));
		}
	}

	void removePlayer(final LobbyPlayer player)
	{
		aLobbyPlayers.remove(player);
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("gamedata", aGameData).setListAttribute("players", aLobbyPlayers);
	}
}

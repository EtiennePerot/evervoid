package com.evervoid.server;

import com.evervoid.json.Json;
import com.evervoid.network.lobby.LobbyState;
import com.jme3.network.connection.Client;

public interface EVServerMessageObserver
{
	void clientQuit(Client client);

	void messageReceived(String type, LobbyState lobby, Client client, Json content);

	void stop();
}

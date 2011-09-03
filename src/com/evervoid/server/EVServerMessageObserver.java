package com.evervoid.server;

import com.evervoid.json.Json;
import com.evervoid.network.lobby.LobbyState;
import com.jme3.network.connection.Client;

/**
 * An interface that any class wishing to observer {@link EverVoidServer} should implement.
 */
public interface EVServerMessageObserver
{
	/**
	 * Notifies the observer that a Client has quit
	 * 
	 * @param client
	 *            The Client that has quit
	 */
	void clientQuit(Client client);

	/**
	 * Notifies the observer that Server has received a message
	 * 
	 * @param type
	 *            The type of the received message
	 * @param lobby
	 *            The current state of the Lobby
	 * @param client
	 *            The Client initiating this message
	 * @param content
	 *            The content of the Message
	 */
	void messageReceived(String type, LobbyState lobby, Client client, Json content);

	/**
	 * Notifies the observer that the Server has stopped
	 */
	void stop();
}

package com.evervoid.server;

import com.evervoid.json.Json;
import com.evervoid.network.lobby.LobbyState;
import com.jme3.network.Client;
import com.jme3.network.HostedConnection;

/**
 * An interface that any class wishing to observer {@link EverVoidServer} should implement.
 */
public interface EVGameMessageObserver
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
	 * @param source
	 *            The Client initiating this message
	 * @param content
	 *            The content of the Message
	 */
	void messageReceived(String type, LobbyState lobby, HostedConnection source, Json content);

	/**
	 * Notifies the observer that the Server has stopped
	 */
	void serverStopped();
}

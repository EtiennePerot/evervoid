package com.evervoid.client.interfaces;

import com.evervoid.server.LobbyState;

public interface EVLobbyMessageListener
{
	/**
	 * Called when new Lobby Data Message received.
	 * 
	 * @param lobbyData
	 *            The Data received.
	 */
	public void receivedLobbyData(LobbyState lobbyData);

	/**
	 * Called when the EverVoid Start Game message has been received.
	 */
	public void receivedStartGame();
}

package com.evervoid.client.interfaces;

import com.evervoid.json.Json;
import com.evervoid.server.LobbyState;

public interface EVLobbyMessageListener
{
	/**
	 * Called when new Game Data received.
	 * 
	 * @param gameData
	 */
	public void receivedGameData(Json gameData);

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

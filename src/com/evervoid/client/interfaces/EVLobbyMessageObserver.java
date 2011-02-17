package com.evervoid.client.interfaces;

import com.evervoid.json.Json;

public interface EVLobbyMessageObserver
{
	/**
	 * Called when new Game Data received.
	 * 
	 * @param gameData
	 */
	public void receivedGameData(Json gameData);

	/**
	 * Called when new Player Data Message received.
	 * 
	 * @param playerData
	 *            The Data received.
	 */
	public void receivedPlayerData(Json playerData);

	/**
	 * Called when the EverVoid Start Game message has been received.
	 */
	public void receivedStartGame();
}

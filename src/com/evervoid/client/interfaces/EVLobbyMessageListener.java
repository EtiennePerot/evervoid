package com.evervoid.client.interfaces;

import com.evervoid.network.lobby.LobbyState;
import com.evervoid.state.Color;

public interface EVLobbyMessageListener
{
	/**
	 * Called when the EverVoid Chat Message is received.
	 * 
	 * @param player
	 *            The name of the player who sent the message
	 * @param color
	 *            The color of the player who sent the message
	 * @param message
	 *            The message string
	 */
	public void receivedChat(String player, Color playerColor, String message);

	/**
	 * Called when new Lobby Data Message received.
	 * 
	 * @param lobbyData
	 *            The Data received.
	 * @param localPlayer
	 *            The name of the local player
	 */
	public void receivedLobbyData(LobbyState lobbyData, String localPlayer);

	/**
	 * Called when the EverVoid Start Game message has been received.
	 */
	public void receivedStartGame();

	/**
	 * Called when Server dies.
	 */
	public void serverDied();
}

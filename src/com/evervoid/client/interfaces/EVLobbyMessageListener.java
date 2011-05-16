package com.evervoid.client.interfaces;

import com.evervoid.network.lobby.LobbyState;
import com.evervoid.state.Color;

/**
 * Classes implementing the EVLobbyMessageListener interface can receive notifications about lobby-specific messages.
 */
public interface EVLobbyMessageListener
{
	/**
	 * Called when the EverVoid Chat Message is received.
	 * 
	 * @param player
	 *            The name of the player who sent the message
	 * @param playerColor
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
	 */
	public void receivedLobbyData(LobbyState lobbyData);

	/**
	 * Called when the EverVoid Start Game message has been received.
	 */
	public void receivedStartGame();

	/**
	 * Called when the server dies.
	 */
	public void serverDied();
}

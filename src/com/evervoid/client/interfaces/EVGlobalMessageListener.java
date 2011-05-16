package com.evervoid.client.interfaces;

import com.evervoid.json.Json;
import com.evervoid.state.Color;
import com.evervoid.state.EVGameState;

/**
 * Classes implementing the EVGlobalMessageListener interface will receive global-scope messages from the server
 */
public interface EVGlobalMessageListener
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
	 * Called when an EverVoid GameState Message is received.
	 * 
	 * @param gameState
	 *            The Game State received.
	 * @param playerName
	 *            The name of the local player.
	 */
	public void receivedGameState(EVGameState gameState, String playerName);

	/**
	 * Called when a EverVoid Quit Message is received.
	 * 
	 * @param quitMessage
	 *            The player quitting and reasons.
	 */
	public void receivedQuit(Json quitMessage);
}

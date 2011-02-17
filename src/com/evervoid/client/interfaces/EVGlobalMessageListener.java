package com.evervoid.client.interfaces;

import com.evervoid.json.Json;

public interface EVGlobalMessageListener
{
	/**
	 * Called when the EverVoid Chat Message is received.
	 * 
	 * @param chatMessage
	 *            The contents of the chat.
	 */
	public void receivedChat(Json chatMessage);

	/**
	 * Called when and EverVoid GameState Message is received.
	 * 
	 * @param gameState
	 *            The Game State received.
	 */
	public void receivedGameState(Json gameState);

	/**
	 * Called when a pong message is received.
	 * 
	 * @param packet
	 *            The contents of the pong.
	 */
	public void receivedPong(Json packet);

	/**
	 * Called when a EverVoid Quit Message is received.
	 * 
	 * @param quitMessage
	 *            The player quitting and reasons.
	 */
	public void receivedQuit(Json quitMessage);
}

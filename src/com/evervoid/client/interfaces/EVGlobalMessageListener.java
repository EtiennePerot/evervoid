package com.evervoid.client.interfaces;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;

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
	 * Called when an EverVoid GameState Message is received.
	 * 
	 * @param gameState
	 *            The Game State received.
	 */
	public void receivedGameState(EVGameState gameState);

	/**
	 * Called when a EverVoid Quit Message is received.
	 * 
	 * @param quitMessage
	 *            The player quitting and reasons.
	 */
	public void receivedQuit(Json quitMessage);
}

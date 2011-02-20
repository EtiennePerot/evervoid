package com.evervoid.client.interfaces;

import com.evervoid.json.Json;

public interface EVGameMessageListener
{
	/**
	 * Called when a new EverVoid Turn Message is received.
	 * 
	 * @param turn
	 *            The turn received.
	 */
	public void receivedTurn(Json turn);
}
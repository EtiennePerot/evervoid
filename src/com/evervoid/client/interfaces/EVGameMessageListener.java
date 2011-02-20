package com.evervoid.client.interfaces;

import com.evervoid.state.action.Turn;

public interface EVGameMessageListener
{
	/**
	 * Called when a new EverVoid Turn Message is received.
	 * 
	 * @param turn
	 *            The turn received.
	 */
	public void receivedTurn(Turn turn);
}

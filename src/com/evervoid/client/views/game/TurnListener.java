package com.evervoid.client.views.game;

/**
 * A node implementing TurnListener indicates that it receives events when a turn is sent, received, and played back.
 */
public interface TurnListener
{
	/**
	 * Called when a turn is fully played back and the player may resume playing.
	 */
	public void turnPlayedBack();

	/**
	 * Called when a turn is received.
	 */
	public void turnReceived();

	/**
	 * Called when a turn is sent.
	 */
	public void turnSent();
}

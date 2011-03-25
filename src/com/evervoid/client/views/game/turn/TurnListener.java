package com.evervoid.client.views.game.turn;

/**
 * A node implementing TurnListener indicates that it receives events when a turn is sent, received, and played back.
 */
public interface TurnListener
{
	/**
	 * Called when a turn is fully played back.
	 */
	public void turnPlayedback();

	/**
	 * Called when a turn is received.
	 */
	public void turnReceived(TurnSynchronizer synchronizer);

	/**
	 * Called when a turn is sent.
	 */
	public void turnSent();
}

package com.evervoid.client.views.game;

/**
 * A node implementing Freezable indicates that it can be "frozen" while waiting for the Turn to come back from the server.
 */
public interface Freezable
{
	/**
	 * Called when the node should be frozen.
	 */
	public void freeze();

	/**
	 * Called when the node should be unfrozen.
	 */
	public void unfreeze();
}

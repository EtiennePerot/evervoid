package com.evervoid.state.observers;

import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;

/**
 * PlayerObserver is a template for Objects wishing to observer {@link Player}. Players will broadcast to all their observers
 * when appropriate. The first parameter of the broadcasting methods will be the Player sending the message, in order to allow
 * Objects to observer multiple Players.
 */
public interface PlayerObserver
{
	/**
	 * Called when a Player has just been defeated, as determined by the GameEngine.
	 * 
	 * @param broadcastingPlayer
	 *            The Player that has just been defeated.
	 */
	public void playerDefeat(Player broadcastingPlayer);

	/**
	 * Called when a Player disconnects from the game.
	 * 
	 * @param broadcastingPlayer
	 *            The Player disconnecting from the game.
	 */
	public void playerDisconnected(Player broadcastingPlayer);

	/**
	 * Called when a player receives income.
	 * 
	 * @param broadcastingPlayer
	 *            The PLayer receiving income.
	 * @param amount
	 *            The amount of income the Player is receiving.
	 */
	public void playerReceivedIncome(Player broadcastingPlayer, ResourceAmount amount);

	/**
	 * Called when a Player wins the game.
	 * 
	 * @param broadcastingPlayer
	 *            The Player that has just won the game.
	 */
	public void playerWon(Player broadcastingPlayer);
}

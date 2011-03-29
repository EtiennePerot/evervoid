package com.evervoid.client.interfaces;

import com.evervoid.state.action.Turn;
import com.evervoid.state.player.Player;

public interface EVGameMessageListener
{
	/**
	 * Called when a player loses the game.
	 * 
	 * @param player
	 *            The player who lost the game.
	 */
	public void playerLost(Player loser);

	/**
	 * Called when a player wins the game.
	 * 
	 * @param player
	 *            The player who won the game.
	 */
	public void playerWon(Player winner);

	/**
	 * Called when a new EverVoid Turn Message is received.
	 * 
	 * @param turn
	 *            The turn received.
	 */
	public void receivedTurn(Turn turn);
}

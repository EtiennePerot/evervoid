package com.evervoid.network;

import com.evervoid.state.EVGameState;

/**
 * Game state message; stores an entire game state
 */
public class GameStateMessage extends EverMessage
{
	/**
	 * Constructor; serializes a game state and prepares it for sending
	 * 
	 * @param state
	 *            The game state
	 */
	public GameStateMessage(final EVGameState state)
	{
		super(state, "gamestate");
	}

	/**
	 * Deserializes the game state
	 * 
	 * @return The deserialized game state
	 */
	public EVGameState getState()
	{
		return new EVGameState(getJson());
	}
}

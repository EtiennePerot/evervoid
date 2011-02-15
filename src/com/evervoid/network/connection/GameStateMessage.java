package com.evervoid.network.connection;

import com.evervoid.state.EverVoidGameState;

/**
 * Game state message; stores an entire game state
 */
public class GameStateMessage extends JsonMessage
{
	/**
	 * Constructor; serializes a game state and prepares it for sending
	 * 
	 * @param state
	 *            The game state
	 */
	public GameStateMessage(final EverVoidGameState state)
	{
		super(state);
	}

	/**
	 * Deserializes the game state
	 * 
	 * @return The deserialized game state
	 */
	public EverVoidGameState getState()
	{
		return new EverVoidGameState(getJson());
	}
}

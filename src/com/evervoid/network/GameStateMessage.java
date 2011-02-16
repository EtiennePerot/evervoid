package com.evervoid.network;

import com.evervoid.state.EverVoidGameState;

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
	public GameStateMessage(final EverVoidGameState state)
	{
		super(state, "gamestate");
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

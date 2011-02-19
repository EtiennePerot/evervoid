package com.evervoid.network.message;

import com.evervoid.state.EVGameState;

/**
 * Message containing an entire game state
 */
public class GameStateMessage extends EverMessage
{
	public GameStateMessage(final EVGameState state)
	{
		super(state, "gamestate");
	}

	public EVGameState getGameState()
	{
		return new EVGameState(getJson());
	}
}

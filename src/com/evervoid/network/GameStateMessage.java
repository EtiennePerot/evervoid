package com.evervoid.network;

import com.evervoid.state.EVGameState;
import com.evervoid.state.data.BadJsonInitialization;

/**
 * Message containing an entire game state
 */
public class GameStateMessage extends EverMessage
{
	public GameStateMessage(final EVGameState state)
	{
		super(state, GameStateMessage.class.getName());
	}

	public EVGameState getGameState() throws BadJsonInitialization
	{
		return new EVGameState(getJson());
	}
}

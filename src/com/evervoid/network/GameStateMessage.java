package com.evervoid.network;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.data.BadJsonInitialization;

/**
 * Message containing an entire game state
 */
public class GameStateMessage extends EverMessage
{
	public GameStateMessage(final EVGameState state, final String localPlayer)
	{
		super(new Json().setAttribute("state", state).setStringAttribute("player", localPlayer));
	}

	public EVGameState getGameState() throws BadJsonInitialization
	{
		return new EVGameState(getJson());
	}
}

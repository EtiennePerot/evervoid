package com.evervoid.network.message;

import com.evervoid.json.BadJsonInitialization;
import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;
import com.evervoid.state.EVGameState;

/**
 * Message containing an entire game state
 */
public class GameStateMessage extends EVMessage
{
    public GameStateMessage(final EVGameState state, final String localPlayer)
    {
        super(new Json().setAttribute("state", state).setAttribute("player", localPlayer));
    }

    public GameStateMessage(final Json json)
    {
        super(json);
    }

    public EVGameState getGameState() throws BadJsonInitialization
    {
        return new EVGameState(getContent());
    }
}

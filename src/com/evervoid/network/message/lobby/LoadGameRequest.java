package com.evervoid.network.message.lobby;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;

public class LoadGameRequest extends LobbyMessage
{
    public LoadGameRequest(final EVGameState savedState)
    {
        super(savedState);
    }

    protected LoadGameRequest(final Json json)
    {
        super(json);
    }
}

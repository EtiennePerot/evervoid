package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;
import com.evervoid.state.EVGameState;

public class SaveGameStateReply extends EVMessage
{
    public SaveGameStateReply(final EVGameState savable)
    {
        super(savable);
    }

    public SaveGameStateReply(final Json json)
    {
        super(json);
    }
}

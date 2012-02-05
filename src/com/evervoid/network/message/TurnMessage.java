package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;
import com.evervoid.state.action.Turn;

public class TurnMessage extends EVMessage
{
    public TurnMessage(final Json json)
    {
        super(json);
    }

    public TurnMessage(final Turn turn)
    {
        super(turn);
    }
}

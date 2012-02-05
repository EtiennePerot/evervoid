package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;

public class ReadyMessage extends EVMessage
{
    public ReadyMessage()
    {
        super(new Json());
    }

    public ReadyMessage(final Json json)
    {
        super(json);
    }
}

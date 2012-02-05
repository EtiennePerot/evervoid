package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;

public class JoinErrorMessage extends EVMessage
{
    public JoinErrorMessage(final Json json)
    {
        super(json);
    }

    public JoinErrorMessage(final String errorMessage)
    {
        super(new Json(errorMessage));
    }
}

package com.evervoid.network.message.lobby;

import com.evervoid.json.Json;

public class StartGameMessage extends LobbyMessage
{
    public StartGameMessage()
    {
        super(new Json());
    }
}

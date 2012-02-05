package com.evervoid.network.message.lobby;

import com.evervoid.json.Json;
import com.evervoid.network.lobby.LobbyPlayer;

public class LobbyPlayerUpdate extends LobbyMessage
{
    public LobbyPlayerUpdate(final Json json)
    {
        super(json);
    }

    public LobbyPlayerUpdate(final LobbyPlayer player)
    {
        super(player);
    }
}

package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;
import com.evervoid.network.lobby.LobbyPlayer;
import com.evervoid.network.lobby.LobbyState;

public class LobbyStateMessage extends EVMessage
{
    public LobbyStateMessage(final Json json)
    {
        super(json);
    }

    public LobbyStateMessage(final LobbyState lobbyState, final LobbyPlayer player)
    {
        super(lobbyState.getJsonForPlayer(player));
    }
}

package com.evervoid.network.message.lobby;

import com.evervoid.json.Jsonable;
import com.evervoid.network.EVMessage;
import com.evervoid.server.EVNetworkEngine;

/**
 * LobbyMessages are all the {@link EVMessage}s that can be sent between clients and servers while the players are in
 * the lobby. These messages are handled by the {@link EVNetworkEngine} in the handleLobbyMessage(...) method.
 */
public abstract class LobbyMessage extends EVMessage
{

    protected LobbyMessage(final Jsonable content)
    {
        super(content);
    }

}

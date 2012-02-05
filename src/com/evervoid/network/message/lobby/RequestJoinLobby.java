package com.evervoid.network.message.lobby;

import com.evervoid.json.Json;
import com.jme3.network.serializing.Serializable;

/**
 * Handshake message sent by the client when first connecting to a server
 */
@Serializable
public class RequestJoinLobby extends LobbyMessage
{
    public RequestJoinLobby(final Json json)
    {
        super(json);
    }

    public RequestJoinLobby(final String nickname)
    {
        super(new Json().setAttribute("nickname", nickname));
    }
}

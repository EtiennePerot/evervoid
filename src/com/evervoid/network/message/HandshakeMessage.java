package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Handshake message sent by the client when first connecting to a server
 */
@Serializable
public class HandshakeMessage extends EVMessage
{
	public HandshakeMessage(final String nickname)
	{
		super(new Json().setAttribute("nickname", nickname));
	}
}

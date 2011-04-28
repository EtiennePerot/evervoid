package com.evervoid.network;

import com.evervoid.json.Json;
import com.jme3.network.serializing.Serializable;

/**
 * Handshake message sent by the client when first connecting to a server
 */
@Serializable
public class HandshakeMessage extends EverMessage
{
	public HandshakeMessage(final String nickname)
	{
		super(new Json().setAttribute("nickname", nickname));
	}
}

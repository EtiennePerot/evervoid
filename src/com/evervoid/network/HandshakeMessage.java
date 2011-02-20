package com.evervoid.network;

import com.evervoid.json.Json;
import com.jme3.network.serializing.Serializable;

/**
 * Handshake message sent by the client when first connecting to a server
 */
@Serializable
public class HandshakeMessage extends EverMessage
{
	// This should probably take some kind of (Jsonable) player data as argument
	public HandshakeMessage()
	{
		super(new Json(), "handshake");
	}
}

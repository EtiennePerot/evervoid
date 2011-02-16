package com.evervoid.network;

import com.evervoid.json.Jsonable;

/**
 * Handshake message sent by the client when first connecting to a server
 */
public class Handshake extends EverMessage
{
	// This shouldn't take a Jsonable object; probably some kind of (Jsonable) player data instead
	public Handshake(final Jsonable jsonableObject)
	{
		super(jsonableObject, "handshake");
	}
}

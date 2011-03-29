package com.evervoid.network;

import com.evervoid.json.Json;

/**
 * Sent by clients, this message asks servers about general info (server name, number of players, etc) without required
 * authentication.
 */
public class RequestServerInfo extends EverMessage
{
	public RequestServerInfo()
	{
		super(new Json(), RequestServerInfo.class.getName());
	}
}

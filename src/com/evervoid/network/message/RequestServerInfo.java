package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;

/**
 * Sent by clients, this message asks servers about general info (server name, number of players, etc) without required
 * authentication.
 */
public class RequestServerInfo extends EVMessage
{
	public RequestServerInfo()
	{
		super(new Json());
	}
}

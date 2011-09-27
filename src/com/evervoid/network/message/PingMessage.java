package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;

public class PingMessage extends EVMessage
{
	public PingMessage()
	{
		super(new Json(new Long(System.currentTimeMillis())));
	}
}

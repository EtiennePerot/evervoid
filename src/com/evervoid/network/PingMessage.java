package com.evervoid.network;

import com.evervoid.json.Json;

public class PingMessage extends EverMessage
{
	public PingMessage()
	{
		super(new Json(new Long(System.currentTimeMillis())));
	}
}

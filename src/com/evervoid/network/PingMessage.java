package com.evervoid.network;

import com.evervoid.json.Json;

public class PingMessage extends EverMessage
{
	public PingMessage()
	{
		super(new Json(System.currentTimeMillis()));
	}
}

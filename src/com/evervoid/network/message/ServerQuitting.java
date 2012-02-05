package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;

public class ServerQuitting extends EVMessage
{
	public ServerQuitting()
	{
		super(new Json());
	}
}

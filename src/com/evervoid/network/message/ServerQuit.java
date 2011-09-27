package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;

public class ServerQuit extends EVMessage
{
	public ServerQuit()
	{
		super(new Json());
	}
}

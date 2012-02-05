package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;

public class ClientQuit extends EVMessage
{
	public ClientQuit()
	{
		super(new Json());
	}
}

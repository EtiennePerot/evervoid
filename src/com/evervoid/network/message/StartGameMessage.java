package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;

public class StartGameMessage extends EVMessage
{
	public StartGameMessage()
	{
		super(new Json());
	}
}

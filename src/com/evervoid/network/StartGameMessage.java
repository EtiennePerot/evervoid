package com.evervoid.network;

import com.evervoid.json.Json;

public class StartGameMessage extends EverMessage
{
	public StartGameMessage()
	{
		super(new Json(), StartGameMessage.class.getName());
	}
}

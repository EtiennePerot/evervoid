package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.network.EVMessage;

public class ReadyMessage extends EVMessage
{
	public ReadyMessage()
	{
		super(new Json());
	}

	public ReadyMessage(final Jsonable content)
	{
		super(content);
	}
}

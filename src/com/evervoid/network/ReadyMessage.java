package com.evervoid.network;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class ReadyMessage extends EverMessage
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

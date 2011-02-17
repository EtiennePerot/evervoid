package com.evervoid.network;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class EverMessage
{
	private final Json aJson;
	private final String aType;

	public EverMessage(final EverCompressedMessage message)
	{
		aJson = Json.fromString(message.getMessage().getContent());
		aType = message.getMessage().getType();
	}

	public EverMessage(final Jsonable content, final String messageType)
	{
		aJson = content.toJson();
		aType = messageType;
	}

	public Json getJson()
	{
		return aJson;
	}

	EverCompressedMessage getMessage()
	{
		return new EverCompressedMessage(aJson.toString(), aType);
	}

	public String getType()
	{
		return aType;
	}
}

package com.evervoid.network;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class EverMessage
{
	private final Json aJson;
	private final String aType;

	public EverMessage(final CompressedMessage message)
	{
		aJson = Json.fromString(message.getMessageContents());
		aType = message.getType();
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

	CompressedMessage getMessage()
	{
		return new CompressedMessage(aJson.toString(), aType);
	}

	public String getType()
	{
		return aType;
	}
}

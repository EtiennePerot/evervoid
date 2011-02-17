package com.evervoid.network;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.jme3.network.message.GZIPCompressedMessage;
import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializable;

@Serializable
public class EverMessage extends GZIPCompressedMessage
{
	public EverMessage()
	{
	}

	public EverMessage(final InnerMessage message)
	{
		setMessage(message);
	}

	public EverMessage(final Jsonable content, final String messageType)
	{
		this(new InnerMessage(content.toJson().toString(), messageType));
	}

	public Json getJson()
	{
		return Json.fromString(getMessage().getContent());
	}

	@Override
	public InnerMessage getMessage()
	{
		return (InnerMessage) super.getMessage();
	}

	public String getType()
	{
		return getMessage().getType();
	}

	@Override
	public void setMessage(final Message message)
	{
		if (message instanceof InnerMessage) {
			super.setMessage(message);
		}
	}
}

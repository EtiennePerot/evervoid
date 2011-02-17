package com.evervoid.network;

import com.jme3.network.message.GZIPCompressedMessage;
import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializable;

@Serializable
public class EverCompressedMessage extends GZIPCompressedMessage
{
	public EverCompressedMessage()
	{
	}

	public EverCompressedMessage(final InnerMessage message)
	{
		setMessage(message);
	}

	public EverCompressedMessage(final String content, final String messageType)
	{
		this(new InnerMessage(content, messageType));
	}

	@Override
	public InnerMessage getMessage()
	{
		return (InnerMessage) super.getMessage();
	}

	@Override
	public void setMessage(final Message message)
	{
		if (message instanceof InnerMessage) {
			super.setMessage(message);
		}
	}
}

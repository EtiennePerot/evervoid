package com.evervoid.network;

import com.jme3.network.message.GZIPCompressedMessage;
import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializable;

/**
 * This is the main message class. All messages that are actually sent are of this type. However, it should never be used
 * directly either. It is public because jMonkeyEngine's deserializer needs to access it.
 */
@Serializable
public class EverCompressedMessage extends GZIPCompressedMessage
{
	/**
	 * This argument-less public constructor is necessary for deserialization on the SpiderMonkey side.
	 */
	public EverCompressedMessage()
	{
	}

	/**
	 * Create a new EverCompressedMessage
	 * 
	 * @param content
	 *            The message content (as a String)
	 * @param messageType
	 *            The message type
	 */
	EverCompressedMessage(final String content, final String messageType)
	{
		setMessage(new InnerMessage(content, messageType));
	}

	/**
	 * @return The contents of this message
	 */
	String getContent()
	{
		return getMessage().getContent();
	}

	/**
	 * Shadow GZIPCompressedMessage's getMessage() method to return an InnerMessage, as we are certain it will always be one
	 */
	@Override
	public InnerMessage getMessage()
	{
		return (InnerMessage) super.getMessage();
	}

	/**
	 * @return The type of this message
	 */
	String getType()
	{
		return getMessage().getType();
	}

	/**
	 * Shadow GZIPCompressedMessage's setMessage() to only allow InnerMessages to be set
	 */
	@Override
	public void setMessage(final Message message)
	{
		if (message instanceof InnerMessage) {
			super.setMessage(message);
		}
	}
}

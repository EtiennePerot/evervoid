package com.evervoid.network;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.jme3.network.message.GZIPCompressedMessage;
import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializable;

/**
 * This is the main message class. All messages that are actually sent are of this type. It is abstract because it should never
 * be used directly. Subclasses should define their own serialization/deserialization methods, to give transparently serialized
 * messages.
 */
@Serializable
public abstract class EverMessage extends GZIPCompressedMessage
{
	/**
	 * This argument-less constructor is necessary for deserialization on the SpiderMonkey side.
	 */
	public EverMessage()
	{
	}

	/**
	 * Create a new EverMessage
	 * 
	 * @param content
	 *            The message content (a Jsonable object)
	 * @param messageType
	 *            The message type
	 */
	public EverMessage(final Jsonable content, final String messageType)
	{
		setMessage(new InnerMessage(content.toJson().toString(), messageType));
	}

	/**
	 * Called by the receiver of the message.
	 * 
	 * @return The parsed Json in this message
	 */
	protected Json getJson()
	{
		return Json.fromString(getMessage().getContent());
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
	 * Called by the receiver of the message.
	 * 
	 * @return The type of this message
	 */
	public String getType()
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

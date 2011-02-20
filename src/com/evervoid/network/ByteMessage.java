package com.evervoid.network;

import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializable;

/**
 * This is a non-compressed message that holds an array of bytes. That's all it does. This class is the one that gets compressed
 * by PartialMessage. It should NEVER be used directly. It is only public because it needs to be, as jMonkey's deserializer
 * needs to be able to access it.
 */
@Serializable
public class ByteMessage extends Message
{
	// This must NEVER be final; don't let the formatter add them!
	private byte[] aMessage;

	/**
	 * This argument-less public constructor is necessary for deserialization on the SpiderMonkey side.
	 */
	public ByteMessage()
	{
	}

	/**
	 * Initialize an InnerMessage with a given content and type
	 * 
	 * @param content
	 *            The message content
	 */
	public ByteMessage(final byte[] content)
	{
		aMessage = content;
	}

	/**
	 * @return The message's content
	 */
	public byte[] getContent()
	{
		return aMessage;
	}
}
package com.evervoid.network;

import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializable;

/**
 * This is a non-compressed message that holds two strings: A content one and a type one. The content one is supposed to hold
 * JSON. This class is the one that gets compressed by EverMessage. It should NEVER be used directly. It is only public because
 * it needs to be, as jMonkey's deserializer needs to be able to access it.
 */
@Serializable
public class InnerMessage extends Message
{
	// They must NEVER be final; don't let the formatter add them!
	private String aMessage = "";
	private String aType = "";

	/**
	 * This argument-less constructor is necessary for deserialization on the SpiderMonkey side.
	 */
	public InnerMessage()
	{
	}

	/**
	 * Initialize an InnerMessage with a given content and type
	 * 
	 * @param content
	 *            The message content
	 * @param messageType
	 *            The message type
	 */
	public InnerMessage(final String content, final String messageType)
	{
		aMessage = content;
		aType = messageType;
	}

	/**
	 * @return The message's content
	 */
	public String getContent()
	{
		return aMessage;
	}

	/**
	 * @return The message's type
	 */
	public String getType()
	{
		return aType;
	}
}
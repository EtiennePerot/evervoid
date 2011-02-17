package com.evervoid.network;

import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializable;

@Serializable
public class InnerMessage extends Message
{
	// Attributes can be public and without getters here because this is a private inner class
	// They must NEVER be final; don't let the formatter add them!
	private String aMessage = "";
	private String aType = "";

	/**
	 * This argument-less constructor is necessary for deserialization on the SpiderMonkey side.
	 */
	public InnerMessage()
	{
	}

	public InnerMessage(final String content, final String messageType)
	{
		aMessage = content;
		aType = messageType;
	}

	public String getContent()
	{
		return aMessage;
	}

	public String getType()
	{
		return aType;
	}
}
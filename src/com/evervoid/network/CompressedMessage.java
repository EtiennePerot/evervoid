package com.evervoid.network;

import com.jme3.network.message.GZIPCompressedMessage;
import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializable;

/**
 * Basic EverVoid message class. Always compressed.
 */
@Serializable(id = 1337)
public class CompressedMessage extends GZIPCompressedMessage
{
	private class InnerMessage extends Message
	{
		// Attributes can be public and without getters here because this is a private inner class
		// They must NEVER be final; don't let the formatter add them!
		public String aMessage = "";
		public String aType = "";

		/**
		 * This argument-less constructor is necessary for deserialization on the SpiderMonkey side.
		 */
		@SuppressWarnings("unused")
		public InnerMessage()
		{
		}

		public InnerMessage(final String content, final String messageType)
		{
			aMessage = content;
			aType = messageType;
		}
	}

	/**
	 * This argument-less constructor is necessary for deserialization on the SpiderMonkey side.
	 */
	public CompressedMessage()
	{
	}

	public CompressedMessage(final String messageContent, final String messageType)
	{
		setMessage(new InnerMessage(messageContent, messageType));
	}

	/**
	 * @return The contents of this message, or null if it is not defined
	 */
	protected String getMessageContents()
	{
		final InnerMessage msg = (InnerMessage) getMessage();
		if (msg == null) {
			return null;
		}
		return msg.aMessage;
	}

	/**
	 * Called by the receiver of this message
	 * 
	 * @return The type of this message, or null if it is not defined
	 */
	public String getType()
	{
		final InnerMessage msg = (InnerMessage) getMessage();
		if (msg == null) {
			return null;
		}
		return msg.aType;
	}
}

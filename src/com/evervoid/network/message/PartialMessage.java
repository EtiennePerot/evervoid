package com.evervoid.network.message;

import java.io.UnsupportedEncodingException;

import com.evervoid.json.Json;
import com.jme3.network.message.GZIPCompressedMessage;
import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializable;

/**
 * This is the main message class. All messages that are actually sent are of this type. However, it should never be used
 * directly either. It is public because jMonkeyEngine's deserializer needs to access it.
 */
@Serializable
public class PartialMessage extends GZIPCompressedMessage
{
	static final int sMaxPartialMessageSize = 8192;
	private Json aDecodedMessage = null;

	/**
	 * This argument-less public constructor is necessary for deserialization on the SpiderMonkey side.
	 */
	public PartialMessage()
	{
	}

	/**
	 * Create a new EverCompressedMessage
	 * 
	 * @param content
	 *            The partial message content (as a String)
	 * @param messageType
	 *            The message type of the overall message
	 * @param messageHash
	 *            The hash of the final message
	 * @param messagePart
	 *            The part number that this partial message is
	 * @param totalParts
	 *            The total number of parts of the overall message
	 */
	PartialMessage(final String content, final String messageType, final String messageHash, final int messagePart,
			final int totalParts)
	{
		final Json innerJson = new Json().setStringAttribute("content", content).setStringAttribute("type", messageType)
				.setStringAttribute("hash", messageHash).setIntAttribute("part", messagePart)
				.setIntAttribute("total", totalParts);
		try {
			setMessage(new ByteMessage(innerJson.toString().getBytes("UTF8")));
		}
		catch (final UnsupportedEncodingException e) {
			// Never gonna happen, Java supports UTF-8
		}
	}

	/**
	 * Decodes the underlying Json in the ByteMessage
	 */
	private void decode()
	{
		if (aDecodedMessage != null) {
			return;
		}
		try {
			aDecodedMessage = Json.fromString(new String(getMessage().getContent(), "UTF8"));
		}
		catch (final UnsupportedEncodingException e) {
			// Never gonna happen, Java supports UTF-8
		}
	}

	/**
	 * @return The content of this partial message
	 */
	String getContent()
	{
		decode();
		return aDecodedMessage.getStringAttribute("content");
	}

	/**
	 * @return The hash of the overall message
	 */
	String getHash()
	{
		decode();
		return aDecodedMessage.getStringAttribute("hash");
	}

	/**
	 * Shadow GZIPCompressedMessage's getMessage() method to return a ByteMessage, as we are certain it will always be one
	 */
	@Override
	public ByteMessage getMessage()
	{
		return (ByteMessage) super.getMessage();
	}

	/**
	 * @return The part number of this partial message
	 */
	int getPart()
	{
		decode();
		return aDecodedMessage.getIntAttribute("part");
	}

	/**
	 * @return The total number of parts in the overall message
	 */
	int getTotalParts()
	{
		decode();
		return aDecodedMessage.getIntAttribute("total");
	}

	/**
	 * @return The type of the overall message
	 */
	String getType()
	{
		decode();
		return aDecodedMessage.getStringAttribute("type");
	}

	/**
	 * Shadow GZIPCompressedMessage's setMessage() to only allow ByteMessages to be set
	 */
	@Override
	public void setMessage(final Message message)
	{
		if (message instanceof ByteMessage) {
			super.setMessage(message);
		}
	}

	@Override
	public String toString()
	{
		return "PartialMessage of " + getType() + "#" + getHash() + " (Part " + (getPart() + 1) + "/" + getTotalParts() + ")";
	}
}

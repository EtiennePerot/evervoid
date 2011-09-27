package com.evervoid.network;

import java.io.UnsupportedEncodingException;

import com.evervoid.json.Json;
import com.jme3.network.Message;
import com.jme3.network.serializing.Serializable;

/**
 * This is the main message class. All messages that are actually sent are of this type. However, it should never be used
 * directly either. It is public because jMonkeyEngine's deserializer needs to access it.
 */
@Serializable
public class PartialMessage implements Message
{
	/**
	 * The maximum size for all partial messages.
	 */
	static final int sMaxPartialMessageSize = 10240;
	/**
	 * The contents of this message.
	 */
	private byte[] aContent;
	/**
	 * The decoded version of the message.
	 */
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
		final Json innerJson = new Json().setAttribute("content", content).setAttribute("type", messageType)
				.setAttribute("hash", messageHash).setAttribute("part", messagePart).setAttribute("total", totalParts);
		try {
			setContents(innerJson.toString().getBytes("UTF8"));
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
			aDecodedMessage = Json.fromString(new String(getMessage(), "UTF8"));
		}
		catch (final UnsupportedEncodingException e) {
			// Never gonna happen, Java supports UTF-8
		}
	}

	/**
	 * @return The decoded message contained.
	 */
	public String getDecodedMessage()
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
	 * @return The encoded byte message.
	 */
	public byte[] getMessage()
	{
		return aContent;
	}

	/**
	 * @return The part number of this partial message
	 */
	public int getPart()
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

	@Override
	public boolean isReliable()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Sets the content of the message.
	 * 
	 * @param byteMessage
	 *            The byte content to set.
	 */
	public void setContents(final byte[] byteMessage)
	{
		aContent = byteMessage;
	}

	@Override
	public Message setReliable(final boolean f)
	{
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public String toString()
	{
		return "Part of " + getType() + " #" + getHash() + " (" + (getPart() + 1) + "/" + getTotalParts() + ")";
	}
}

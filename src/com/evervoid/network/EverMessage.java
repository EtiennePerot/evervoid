package com.evervoid.network;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.jme3.network.message.Message;

/**
 * The base class of all messages. It encapsulates an EverCompressedMessage, for the silly reason that jMonkeyEngine's
 * deserializer requires to know all message classes in order to deserialize properly, and requires to know them in the same
 * order on both sides of the communication. Using an encapsulation class alleviates this problem. This is abstract because
 * subclasses should implement their own serialization/deserialization, rather than exposing the underlying JSON structure.
 */
public abstract class EverMessage
{
	/**
	 * @param message
	 *            An EverCompressedMessage
	 * @return The type of the given EverCompressedMessage
	 */
	public static String getTypeOf(final Message message)
	{
		return ((EverCompressedMessage) message).getType();
	}

	private final Json aJson;
	private final String aType;

	/**
	 * Restore an EverMessage out of an EverCompressedMessage received from the network
	 * 
	 * @param source
	 *            The EverCompressedMessage received
	 */
	public EverMessage(final EverCompressedMessage source)
	{
		aJson = Json.fromString(source.getContent());
		aType = source.getType();
	}

	/**
	 * Main constructor; used to create new EverMessages. Use .getMessage() to get a corresponding, sendable
	 * EverCompressedMessage.
	 */
	public EverMessage(final Jsonable content, final String messageType)
	{
		aJson = content.toJson();
		aType = messageType;
	}

	/**
	 * Get the parsed Json object contained in this message. Called by subclasses only.
	 * 
	 * @return The parsed Json object inside this message
	 */
	protected Json getJson()
	{
		return aJson;
	}

	/**
	 * When it is time to send this message on the network, it needs to be converted to a generic-type class
	 * EverCompressedMessage
	 * 
	 * @return The corresponding, sendable EverCompressedMessage
	 */
	protected EverCompressedMessage getMessage()
	{
		return new EverCompressedMessage(aJson.toString(), aType);
	}

	/**
	 * @return The type of this message
	 */
	public String getType()
	{
		return aType;
	}
}

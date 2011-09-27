package com.evervoid.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.jme3.network.Message;

/**
 * The base class of all messages. Transparently splits itself into multiple PartialMessages. Subclasses should implement their
 * own serialization/deserialization, rather than exposing the underlying JSON structure.
 */
public class EVMessage implements Message
{
	/**
	 * The Json containing the message.
	 */
	private final Json aJson;
	/**
	 * The type of the message.
	 */
	private String aType;

	/**
	 * Used to create new EverMessages containing lists
	 * 
	 * @param content
	 *            The list of content
	 */
	protected EVMessage(final Collection<? extends Jsonable> content)
	{
		if (content == null) {
			aJson = Json.getNullNode();
		}
		else {
			aJson = new Json(content);
		}
		aType = getClass().getName();
	}

	/**
	 * Main constructor; used to create new EverMessages.
	 * 
	 * @param content
	 *            The (Jsonable) content of the message
	 */
	protected EVMessage(final Jsonable content)
	{
		// Unfortunately, it is necessary to put a wrong type (null), because we can't call getClass() during this()
		this(content, null);
		aType = getClass().getName();
	}

	/**
	 * Package-private constructor; used to create build EverMessages received from the network.
	 * 
	 * @param content
	 *            The (Jsonable) content of the message
	 * @param messageType
	 *            The type of the message
	 */
	protected EVMessage(final Jsonable content, final String messageType)
	{
		if (content == null) {
			aJson = Json.getNullNode();
		}
		else {
			aJson = content.toJson();
		}
		aType = messageType;
	}

	/**
	 * Get the parsed Json object contained in this message. Called by subclasses only.
	 * 
	 * @return The parsed Json object inside this message
	 */
	public Json getJson()
	{
		return aJson;
	}

	/**
	 * When it is time to send this message on the network, it needs to be converted to a list of partial messages
	 * 
	 * @return The list of sendable PartialMessages
	 */
	protected List<PartialMessage> getMessages()
	{
		String jsonString = aJson.toString();
		final String hash = aJson.getHash();
		final List<String> parts = new ArrayList<String>();
		while (jsonString.length() > 0) {
			final int partSize = Math.min(PartialMessage.sMaxPartialMessageSize, jsonString.length());
			parts.add(jsonString.substring(0, partSize));
			jsonString = jsonString.substring(partSize);
		}
		final List<PartialMessage> messages = new ArrayList<PartialMessage>(parts.size());
		int partNumber = 0;
		for (final String part : parts) {
			messages.add(new PartialMessage(part, aType, hash, partNumber, parts.size()));
			partNumber++;
		}
		return messages;
	}

	/**
	 * @return The type of this message
	 */
	public String getType()
	{
		return aType;
	}

	@Override
	public boolean isReliable()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Message setReliable(final boolean f)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString()
	{
		return "EverMessage(Type = " + getType() + "; Hash: " + aJson.getHash() + ")";
	}
}

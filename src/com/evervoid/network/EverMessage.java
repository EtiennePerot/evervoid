package com.evervoid.network;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.jme3.network.message.GZIPCompressedMessage;
import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializable;

/**
 * Basic EverVoid message class. Always compressed.
 */
@Serializable(id = 1)
public abstract class EverMessage extends GZIPCompressedMessage
{
	/**
	 * SpiderMonkey's message compression requires two messages: the actual message, and a "compression wrapper" message for it.
	 * In everVoid, EverMessage is the "compression wrapper", and JsonMessage is the actual message. As we want to deal with
	 * compression transparently, JsonMessage is a private inner class so that nothing else can ever use it. All of the stuff is
	 * done through EverMessage's access methods instead, and we don't need to worry about anything.
	 */
	private class JsonMessage extends Message
	{
		// Attributes can be public and without getters here because this is a private inner class
		public final String aHash;
		public final String aJson;
		public final String aType;

		/**
		 * Builds a new JsonMessage. Stores a Json (Jsonable) object and a message type.
		 * 
		 * @param jsonableObject
		 *            The Json(able) object to store (message contents)
		 * @param messageType
		 *            The message type string
		 */
		public JsonMessage(final Jsonable jsonableObject, final String messageType)
		{
			final Json json = jsonableObject.toJson();
			aJson = json.toString();
			aHash = json.getHash();
			aType = messageType;
		}
	}

	/**
	 * Create a new compressed everVoid message.
	 * 
	 * @param jsonableObject
	 *            The contents of the message, as a Jsonable object
	 * @param messageType
	 *            The type of the message as a string
	 */
	public EverMessage(final Jsonable jsonableObject, final String messageType)
	{
		setMessage(new JsonMessage(jsonableObject, messageType));
	}

	/**
	 * @return The inner, non-compressed JsonMessage
	 */
	private JsonMessage getInnerMessage()
	{
		return (JsonMessage) getMessage();
	}

	/**
	 * Called by the receiver of this message
	 * 
	 * @return The parsed Json object that this message contains
	 */
	public Json getJson()
	{
		final Json json = Json.fromString(getInnerMessage().aJson);
		if (!json.getHash().equals(getInnerMessage().aHash)) {
			return null;
		}
		return json;
	}

	/**
	 * Called by the receiver of this message
	 * 
	 * @return The type of this message
	 */
	public String getType()
	{
		return getInnerMessage().aType;
	}
}

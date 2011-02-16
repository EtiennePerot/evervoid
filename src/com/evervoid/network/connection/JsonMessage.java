package com.evervoid.network.connection;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

/**
 * A compressed Message that holds a Json object. Also passes hash to ensure consistency
 */
public abstract class JsonMessage extends BaseMessage
{
	private final String aJsonHash;
	/**
	 * String representation of the Json object; serialized by jMonkey
	 */
	private final String aJsonString;

	/**
	 * Constructor: Takes a Jsonable object and stores it as string
	 * 
	 * @param object
	 *            The Jsonable object to store
	 */
	public JsonMessage(final Jsonable object)
	{
		final Json json = object.toJson();
		aJsonString = json.toString();
		aJsonHash = json.getHash();
	}

	/**
	 * Get back the parsed Json object that we serialized prior to sending. Only subclasses should call this, and deserialize
	 * based on it
	 * 
	 * @return The parsed Json object, or null if there was an error in the communication
	 */
	protected Json getJson()
	{
		final Json restored = Json.fromString(aJsonString);
		if (!restored.getHash().equals(aJsonHash)) {
			return null;
		}
		return restored;
	}
}

package com.evervoid.json;

/**
 * Indicates that this class can be serialized to Json.
 */
public interface Jsonable
{
	/**
	 * @return A Json representation of this instance
	 */
	public Json toJson();
}

package com.evervoid.json;

/**
 * Holds a single parsed Json node, and the length of its original string representation.
 */
class JsonParsingResult
{
	private final Json aNode;
	private final int aOffset;

	/**
	 * Creates a new parsing result
	 * 
	 * @param node
	 *            The parsed Json node
	 * @param offset
	 *            The length of the string representation of the Json node in its original form
	 */
	JsonParsingResult(final Json node, final int offset)
	{
		aNode = node;
		aOffset = offset;
	}

	/**
	 * Creates a new parsing result
	 * 
	 * @param node
	 *            The parsed Json node
	 * @param str
	 *            Determines the lengths of the string representation of the Json
	 */
	JsonParsingResult(final Json node, final String str)
	{
		this(node, str.length());
	}

	/**
	 * @return The parsed Json node
	 */
	Json getJson()
	{
		return aNode;
	}

	/**
	 * @return The length of the Json node's original string representation
	 */
	int getOffset()
	{
		return aOffset;
	}
}

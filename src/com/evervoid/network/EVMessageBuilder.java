package com.evervoid.network;

import com.evervoid.json.Json;

/**
 * This class compiles a list of {@link PartialMessage} into the {@link EVMessage} they represent. The
 */
public class EVMessageBuilder
{
	/**
	 * The assembled partial messages.
	 */
	private final String[] aParts;
	/**
	 * The type of the message being built.
	 */
	private final String aType;

	/**
	 * @param type
	 *            The type of the final message being built.
	 * @param totalParts
	 *            The total number of parts in the final message.
	 */
	public EVMessageBuilder(final String type, final int totalParts)
	{
		aType = type;
		aParts = new String[totalParts];
	}

	/**
	 * Puts the partial message in the spot determined by the message's part field.
	 * 
	 * @param message
	 *            The partial message to place.
	 */
	void addPart(final PartialMessage message)
	{
		aParts[message.getPart()] = message.getDecodedMessage();
	}

	/**
	 * Compiles the final message out of all parts gotten so far
	 * 
	 * @return The final message as an EverMessage, or null if we don't have all the parts yet
	 */
	EVMessage getMessage()
	{
		String finalJson = "";
		for (final String aPart : aParts) {
			if (aPart == null) {
				return null;
			}
			finalJson += aPart;
		}
		// If we get here, we have all parts!
		return new EVMessage(Json.fromString(finalJson), aType);
	}
}

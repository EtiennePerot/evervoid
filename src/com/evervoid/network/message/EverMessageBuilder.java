package com.evervoid.network.message;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.json.Json;
import com.jme3.network.connection.Client;

public class EverMessageBuilder
{
	private Client aClient = null;
	private final Map<Integer, String> aParts = new HashMap<Integer, String>();
	private final int aTotalParts;
	private final String aType;

	public EverMessageBuilder(final String type, final int totalParts)
	{
		aType = type;
		aTotalParts = totalParts;
	}

	void addPart(final PartialMessage message)
	{
		aParts.put(message.getPart(), message.getContent());
		if (aClient == null) {
			aClient = message.getClient();
		}
	}

	/**
	 * Compiles the final message out of all parts gotten so far
	 * 
	 * @return The final message as an EverMessage, or null if we don't have all the parts yet
	 */
	EverMessage getMessage()
	{
		String finalJson = "";
		for (int i = 0; i < aTotalParts; i++) {
			if (!aParts.containsKey(i)) {
				return null;
			}
			finalJson += aParts.get(i);
		}
		// If we get here, we have all parts!
		return new EverMessage(Json.fromString(finalJson), aType).setClient(aClient);
	}
}

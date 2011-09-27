package com.evervoid.network.message;

import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;

/**
 * Simple message that notifies clients that the game is about to start.
 */
public class StartingGameMessage extends EVMessage
{
	public StartingGameMessage()
	{
		super(new Json());
	}
}

package com.evervoid.network;

import com.evervoid.json.Json;

/**
 * Simple message that notifies clients that the game is about to start.
 */
public class StartingGameMessage extends EverMessage
{
	public StartingGameMessage()
	{
		super(new Json());
	}
}

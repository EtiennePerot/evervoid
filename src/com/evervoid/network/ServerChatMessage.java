package com.evervoid.network;

import com.evervoid.state.Color;

/**
 * Chat message sent to players by the server itself, for status messages.
 */
public class ServerChatMessage extends ChatMessage
{
	public ServerChatMessage(final String message)
	{
		super("Server", new Color(1f, 0.5f, 0.5f, 1f, "servermsg"), message);
	}
}

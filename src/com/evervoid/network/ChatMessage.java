package com.evervoid.network;

import com.evervoid.json.Json;

public class ChatMessage extends EverMessage
{
	public ChatMessage(final String username, final String message)
	{
		super(new Json().setStringAttribute("username", username).setStringAttribute("message", message), "chat");
	}
}

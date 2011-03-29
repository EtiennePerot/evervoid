package com.evervoid.network;

import com.evervoid.json.Json;
import com.evervoid.state.Color;

public class ChatMessage extends EverMessage
{
	/**
	 * Client-side constructor: Only send the message
	 * 
	 * @param message
	 *            The message to send
	 */
	public ChatMessage(final String message)
	{
		this(null, null, message);
	}

	/**
	 * Server-side constructor: Holds all the message information
	 * 
	 * @param player
	 *            The player who sent the message
	 * @param playerColor
	 *            The color of that player
	 * @param message
	 *            The message contents
	 */
	public ChatMessage(final String player, final Color playerColor, final String message)
	{
		super(new Json().setStringAttribute("message", message).setStringAttribute("player", player)
				.setAttribute("color", playerColor), ChatMessage.class.getName());
	}
}

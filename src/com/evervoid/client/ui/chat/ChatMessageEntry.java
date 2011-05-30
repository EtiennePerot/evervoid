package com.evervoid.client.ui.chat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.jme3.math.ColorRGBA;

/**
 * An entry in the {@link ScrollingTextArea}. Holds color information
 */
class ChatMessageEntry
{
	/**
	 * Formatting string for timestamps
	 */
	private static final String sChatTimestampFormat = "[HH:mm]";
	/**
	 * The message
	 */
	private final String aMessage;
	/**
	 * The (formatted) timestamp of the message
	 */
	private final String aTimestamp;
	/**
	 * The username who said the message
	 */
	private final String aUsername;
	/**
	 * The color of the username
	 */
	private final ColorRGBA aUsernameColor;

	/**
	 * Simple constructor
	 * 
	 * @param username
	 *            The username who said the message
	 * @param usernameColor
	 *            The color of the username
	 * @param message
	 *            The (formatted) timestamp of the message
	 * @param timestamp
	 *            Whether to show the timestamp of the message or not
	 */
	ChatMessageEntry(final String username, final ColorRGBA usernameColor, final String message, final boolean timestamp)
	{
		aUsername = username;
		aUsernameColor = usernameColor;
		aMessage = message;
		aTimestamp = timestamp ? new SimpleDateFormat(sChatTimestampFormat).format(Calendar.getInstance().getTime()) + " " : "";
	}

	/**
	 * @return The index at which the timestamp part of the total string ends
	 */
	int getTimestampEnd()
	{
		return aTimestamp.length();
	}

	/**
	 * @return The index at which the timestamp part of the total string begins
	 */
	int getTimestampStart()
	{
		return 0;
	}

	/**
	 * @return The color of the username
	 */
	ColorRGBA getUsernameColor()
	{
		return aUsernameColor;
	}

	/**
	 * @return The index at which the username part of the total string ends
	 */
	int getUsernameEnd()
	{
		return aTimestamp.length() + aUsername.length();
	}

	/**
	 * @return The index at which the username part of the total string begins
	 */
	int getUsernameStart()
	{
		return aTimestamp.length();
	}

	/**
	 * @return The complete string that should be displayed in the chat log, containing timestamp (if applicable), username, and
	 *         message.
	 */
	@Override
	public String toString()
	{
		return aTimestamp + aUsername + ": " + aMessage;
	}
}

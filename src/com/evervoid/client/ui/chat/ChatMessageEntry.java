package com.evervoid.client.ui.chat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.jme3.math.ColorRGBA;

class ChatMessageEntry
{
	private static final String sChatTimestampFormat = "[HH:mm]";
	private final String aMessage;
	private final String aTimestamp;
	private final String aUsername;
	private final ColorRGBA aUsernameColor;

	ChatMessageEntry(final String username, final ColorRGBA usernameColor, final String message)
	{
		aUsername = username;
		aUsernameColor = usernameColor;
		aMessage = message;
		aTimestamp = new SimpleDateFormat(sChatTimestampFormat).format(Calendar.getInstance().getTime());
	}

	int getTimestampEnd()
	{
		return aTimestamp.length(); // +1 for extra space
	}

	int getTimestampStart()
	{
		// Only for consistency to look good in the class diagram...
		return 0;
	}

	ColorRGBA getUsernameColor()
	{
		return aUsernameColor;
	}

	int getUsernameEnd()
	{
		return aTimestamp.length() + 1 + aUsername.length();
	}

	int getUsernameStart()
	{
		return aTimestamp.length() + 1; // +1 for extra space
	}

	@Override
	public String toString()
	{
		return aTimestamp + " " + aUsername + ": " + aMessage;
	}
}

package com.evervoid.client.ui.chat;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.views.Bounds;
import com.jme3.font.LineWrapMode;
import com.jme3.math.ColorRGBA;

/**
 * The message-log part of the chat control.
 */
public class ScrollingTextArea extends StaticTextControl
{
	/**
	 * Color of the messages
	 */
	private static final ColorRGBA sChatlogMessageColor = new ColorRGBA(0.7f, 0.7f, 0.7f, 1f);
	/**
	 * Color of the timestamps
	 */
	private static final ColorRGBA sChatLogTimestampColor = new ColorRGBA(0.5f, 0.5f, 0.5f, 1f);
	/**
	 * Maximum height that the log can reach, in pixels; will be automatically adjusted
	 */
	private float aMaxHeight = Float.MAX_VALUE;
	/**
	 * List of messages being displayed, as {@link ChatMessageEntry} to keep track of the different color components
	 */
	private final List<ChatMessageEntry> aMessages = new ArrayList<ChatMessageEntry>();
	/**
	 * Whether to display timestamps or not
	 */
	private boolean aTimestamps = true;

	/**
	 * Default constructor
	 * 
	 * @param timestamps
	 *            Whether to show timestamps or not
	 */
	public ScrollingTextArea(final boolean timestamps)
	{
		super("", sChatlogMessageColor, ButtonControl.sButtonFont, ButtonControl.sButtonFontSize);
		setLineWrapMode(LineWrapMode.Word);
		aTimestamps = timestamps;
	}

	/**
	 * Add a message to the log
	 * 
	 * @param username
	 *            The username who said the message
	 * @param usernameColor
	 *            The color of the username
	 * @param message
	 *            The message that was said
	 */
	void addMessage(final String username, final ColorRGBA usernameColor, final String message)
	{
		aMessages.add(new ChatMessageEntry(username, usernameColor, message, aTimestamps));
		updateDisplay();
		while (getHeight() > aMaxHeight) {
			// Prune oldest message
			aMessages.remove(0);
			updateDisplay();
		}
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		// Never increase aMaxLines
		aMaxHeight = Math.min(aMaxHeight, bounds.height);
		super.setBounds(new Bounds(bounds.x, bounds.y, bounds.width, aMaxHeight));
	}

	/**
	 * Update log area with the current messages in aMessages
	 */
	private void updateDisplay()
	{
		String total = "";
		for (final ChatMessageEntry msg : aMessages) {
			total += msg + "\n";
		}
		setColor(sChatlogMessageColor);
		setText(total.substring(0, total.length() - 1));
		int textOffset = 0;
		for (final ChatMessageEntry msg : aMessages) {
			setColor(textOffset + msg.getTimestampStart(), textOffset + msg.getTimestampEnd(), sChatLogTimestampColor);
			setColor(textOffset + msg.getUsernameStart(), textOffset + msg.getUsernameEnd(), msg.getUsernameColor());
			textOffset += msg.toString().length() + 1; // +1 for extra linebreak
		}
	}
}

package com.evervoid.client.ui.chat;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.views.Bounds;
import com.jme3.math.ColorRGBA;

public class ScrollingTextArea extends StaticTextControl
{
	private static final ColorRGBA sChatlogColor = new ColorRGBA(0.7f, 0.7f, 0.7f, 1f);
	private float aMaxLines = Float.MAX_VALUE;
	private final List<ChatMessageEntry> aMessages = new ArrayList<ChatMessageEntry>();

	public ScrollingTextArea()
	{
		super("", sChatlogColor);
	}

	void addMessage(final String username, final ColorRGBA usernameColor, final String message)
	{
		aMessages.add(new ChatMessageEntry(username, usernameColor, message));
		updateDisplay();
		while (getLines() > aMaxLines) {
			// Prune oldest message
			aMessages.remove(0);
			updateDisplay();
		}
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		aMaxLines = bounds.height / getLineHeight();
	}

	void updateDisplay()
	{
		String total = "";
		for (final ChatMessageEntry msg : aMessages) {
			total += msg + "\n";
		}
		setColor(sChatlogColor);
		setText(total.substring(0, total.length() - 1));
		int textOffset = 0;
		for (final ChatMessageEntry msg : aMessages) {
			setColor(textOffset + msg.getUsernameStart(), textOffset + msg.getUsernameEnd(), msg.getUsernameColor());
			textOffset += msg.toString().length() + 1; // +1 for extra linebreak
		}
	}
}

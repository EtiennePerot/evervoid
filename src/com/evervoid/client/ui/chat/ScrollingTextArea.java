package com.evervoid.client.ui.chat;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.ui.StaticTextControl;
import com.jme3.math.ColorRGBA;

public class ScrollingTextArea extends StaticTextControl
{
	private static final ColorRGBA sChatlogColor = new ColorRGBA(0.7f, 0.7f, 0.7f, 1f);
	private final List<String> aMessages = new ArrayList<String>();

	public ScrollingTextArea()
	{
		super("", sChatlogColor);
	}

	void addMessage(final String message)
	{
		// TODO: Implement message pruning
		aMessages.add(message);
		String total = "";
		for (final String msg : aMessages) {
			total += msg + "\n";
		}
		setText(total.substring(0, total.length() - 1));
	}
}

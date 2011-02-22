package com.evervoid.client.ui.chat;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.views.Bounds;
import com.jme3.math.ColorRGBA;

public class ScrollingTextArea extends StaticTextControl
{
	private static final ColorRGBA sChatlogColor = new ColorRGBA(0.7f, 0.7f, 0.7f, 1f);
	private float aMaxHeight = Float.MAX_VALUE;
	private final List<String> aMessages = new ArrayList<String>();

	public ScrollingTextArea()
	{
		super("", sChatlogColor);
	}

	void addMessage(final String message)
	{
		// TODO: Implement message pruning
		aMessages.add(message);
		updateDisplay();
		while (getHeight() > aMaxHeight) {
			aMessages.remove(0);
			updateDisplay();
		}
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		aMaxHeight = bounds.height;
	}

	void updateDisplay()
	{
		String total = "";
		for (final String msg : aMessages) {
			total += msg + "\n";
		}
		setText(total.substring(0, total.length() - 1));
	}
}

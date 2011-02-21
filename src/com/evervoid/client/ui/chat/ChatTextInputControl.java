package com.evervoid.client.ui.chat;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.ui.TextInputControl;

public class ChatTextInputControl extends TextInputControl
{
	private static final int sMaxMessageLength = 128;
	private final ChatControl aListener;

	public ChatTextInputControl(final ChatControl listener)
	{
		super("", sMaxMessageLength);
		aListener = listener;
	}

	@Override
	public void onKeyPress(final KeyboardKey key)
	{
		if (key.equals(KeyboardKey.ENTER)) {
			aListener.sendMessage();
		}
		else {
			super.onKeyPress(key);
		}
	}
}

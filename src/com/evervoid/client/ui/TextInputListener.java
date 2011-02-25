package com.evervoid.client.ui;

import com.evervoid.client.KeyboardKey;

public interface TextInputListener
{
	public void onTextInputDefocus(TextInputControl control);

	public void onTextInputFocus(TextInputControl control);

	public void onTextInputKey(TextInputControl control, KeyboardKey key);
}

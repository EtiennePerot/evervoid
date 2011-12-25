package com.evervoid.client.ui;

import com.evervoid.client.KeyboardKey;

/**
 * Classes implementing the TextInputListener interface can receive events from {@link TextInputControl}s.
 */
public interface TextInputListener
{
	/**
	 * Called when a {@link TextInputControl} has lost focus.
	 * 
	 * @param control
	 *            The {@link TextInputControl} that lost focus.
	 */
	public void onTextInputDefocus(TextInputControl control);

	/**
	 * Called when a {@link TextInputControl} has gained focus.
	 * 
	 * @param control
	 *            The {@link TextInputControl} that gained focus.
	 */
	public void onTextInputFocus(TextInputControl control);

	/**
	 * Called when a key has been pressed while a {@link TextInputControl} had the focus.
	 * 
	 * @param control
	 *            The {@link TextInputControl} that a key was typed in
	 * @param key
	 *            The {@link KeyboardKey} that was typed
	 */
	public void onTextInputKey(TextInputControl control, KeyboardKey key);
}

package com.evervoid.client.ui;

import com.evervoid.client.KeyboardKey;

/**
 * Classes implementing the UIInputListener interface may receive events from {@link UIControl}s regarding miscellaneous events.
 */
public interface UIInputListener
{
	/**
	 * Called whenever a {@link UIControl} is clicked.
	 * 
	 * @param control
	 *            The {@link UIControl} receiving the click event
	 */
	public void onClick(UIControl control);

	/**
	 * Called whenever a {@link UIControl} loses focus
	 * 
	 * @param control
	 *            The {@link UIControl} losing the focus
	 */
	public void onDefocus(UIControl control);

	/**
	 * Called whenever the user presses a key while this {@link UIControl} has focus
	 * 
	 * @param control
	 *            The {@link UIControl} receiving the key press event
	 * @param key
	 *            The {@link KeyboardKey} that was pressed
	 * @return true if the event has been handled by the listener, false to propagate the event further.
	 */
	public boolean onKeyPress(UIControl control, final KeyboardKey key);

	/**
	 * Called whenever the user releases a key while this {@link UIControl} has focus
	 * 
	 * @param control
	 *            The {@link UIControl} receiving the key release event
	 * @param key
	 *            The {@link KeyboardKey} that was released
	 * @return true if the event has been handled by the listener, false to propagate the event further.
	 */
	public boolean onKeyRelease(UIControl control, final KeyboardKey key);
}

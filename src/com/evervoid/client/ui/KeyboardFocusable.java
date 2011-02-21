package com.evervoid.client.ui;

import com.evervoid.client.KeyboardKey;

public interface KeyboardFocusable
{
	public void defocus();

	public void focus();

	public void onKeyPress(final KeyboardKey key);

	public void onKeyRelease(final KeyboardKey key);
}

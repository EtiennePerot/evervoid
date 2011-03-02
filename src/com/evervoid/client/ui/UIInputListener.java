package com.evervoid.client.ui;

import com.evervoid.client.KeyboardKey;

public interface UIInputListener
{
	public void onDefocus();

	public void onClick();

	public void onKeyPress(final KeyboardKey key);

	public void onKeyRelease(final KeyboardKey key);
}

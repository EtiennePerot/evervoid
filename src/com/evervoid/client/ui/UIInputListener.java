package com.evervoid.client.ui;

import com.evervoid.client.KeyboardKey;

public interface UIInputListener
{
	public void onClick();

	public void onDefocus();

	public boolean onKeyPress(final KeyboardKey key);

	public boolean onKeyRelease(final KeyboardKey key);
}

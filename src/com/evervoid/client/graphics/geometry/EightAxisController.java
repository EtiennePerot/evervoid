package com.evervoid.client.graphics.geometry;

import com.evervoid.client.EVInputManager;
import com.evervoid.client.KeyboardKey;

public class EightAxisController
{
	private boolean aDownPressed = false;
	private int aHorizontalDelta = 0;
	private boolean aLeftPressed = false;
	private boolean aRightPressed = false;
	private boolean aUpPressed = false;
	private int aVerticalDelta = 0;

	public EightAxisController()
	{
		// Nothing
	}

	public int getHorizontalDelta()
	{
		return aHorizontalDelta;
	}

	public int getVerticalDelta()
	{
		return aVerticalDelta;
	}

	public boolean isMoving()
	{
		return aHorizontalDelta != 0 || aVerticalDelta != 0;
	}

	public void onKeyPress(final KeyboardKey key)
	{
		if (EVInputManager.shiftPressed()) {
			return;
		}
		switch (key) {
			case LEFT:
				aLeftPressed = true;
				aHorizontalDelta = (aLeftPressed ? -1 : 0) + (aRightPressed ? 1 : 0);
				break;
			case RIGHT:
				aRightPressed = true;
				aHorizontalDelta = (aLeftPressed ? -1 : 0) + (aRightPressed ? 1 : 0);
				break;
			case UP:
				aUpPressed = true;
				aVerticalDelta = (aDownPressed ? -1 : 0) + (aUpPressed ? 1 : 0);
				break;
			case DOWN:
				aDownPressed = true;
				aVerticalDelta = (aDownPressed ? -1 : 0) + (aUpPressed ? 1 : 0);
				break;
		}
	}

	public void onKeyRelease(final KeyboardKey key)
	{
		if (EVInputManager.shiftPressed()) {
			return;
		}
		switch (key) {
			case LEFT:
				aLeftPressed = false;
				aHorizontalDelta = (aLeftPressed ? -1 : 0) + (aRightPressed ? 1 : 0);
				break;
			case RIGHT:
				aRightPressed = false;
				aHorizontalDelta = (aLeftPressed ? -1 : 0) + (aRightPressed ? 1 : 0);
				break;
			case UP:
				aUpPressed = false;
				aVerticalDelta = (aDownPressed ? -1 : 0) + (aUpPressed ? 1 : 0);
				break;
			case DOWN:
				aDownPressed = false;
				aVerticalDelta = (aDownPressed ? -1 : 0) + (aUpPressed ? 1 : 0);
				break;
		}
	}

	public void reset()
	{
		aLeftPressed = false;
		aRightPressed = false;
		aDownPressed = false;
		aUpPressed = false;
		aHorizontalDelta = 0;
		aVerticalDelta = 0;
	}
}

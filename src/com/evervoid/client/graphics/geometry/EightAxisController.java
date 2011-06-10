package com.evervoid.client.graphics.geometry;

import com.evervoid.client.EVInputManager;
import com.evervoid.client.KeyboardKey;

/**
 * Handles 4-arrow keyboard input events into 8-direction movement
 */
public class EightAxisController
{
	/**
	 * Whether the "Down" arrow key is pressed or not
	 */
	private boolean aDownPressed = false;
	/**
	 * Current horizontal speed (positive or negative)
	 */
	private int aHorizontalDelta = 0;
	/**
	 * Whether the "Left" arrow key is pressed or not
	 */
	private boolean aLeftPressed = false;
	/**
	 * Whether the "Right" arrow key is pressed or not
	 */
	private boolean aRightPressed = false;
	/**
	 * Whether the "Up" arrow key is pressed or not
	 */
	private boolean aUpPressed = false;
	/**
	 * Current vertical speed (positive or negative)
	 */
	private int aVerticalDelta = 0;

	/**
	 * Constructor. Nothing required.
	 */
	public EightAxisController()
	{
		// Nothing
	}

	/**
	 * @return The current horizontal speed (positive for right, negative for left)
	 */
	public int getHorizontalDelta()
	{
		return aHorizontalDelta;
	}

	/**
	 * @return The current vertical speed (positive for up, negative for down)
	 */
	public int getVerticalDelta()
	{
		return aVerticalDelta;
	}

	/**
	 * @return Whether the object should be moving at all or not
	 */
	public boolean isMoving()
	{
		return aHorizontalDelta != 0 || aVerticalDelta != 0;
	}

	/**
	 * Called when a key is pressed.
	 * 
	 * @param key
	 *            The pressed key.
	 */
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

	/**
	 * Called when a key is released.
	 * 
	 * @param key
	 *            The released key.
	 */
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

	/**
	 * Reset this controller and assume that all keys are released.
	 */
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

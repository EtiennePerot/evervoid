package com.evervoid.client;

import com.jme3.math.Vector2f;

/**
 * Manages all keyboard and mouse events and passes them to the correct view in the everVoid view stack. EVInputManger is a
 * singleton.
 */
public class EVInputManager
{
	/**
	 * The singleton instance of the manager.
	 */
	private static EVInputManager sInstance;

	/**
	 * @return The singleton instance of the manager, will create one if none exists.
	 */
	protected static EVInputManager getInstance()
	{
		if (sInstance == null) {
			sInstance = new EVInputManager();
		}
		return sInstance;
	}

	/**
	 * @return Whether shift is currently pressed.
	 */
	public static boolean shiftPressed()
	{
		return getInstance().aShiftPressed;
	}

	/**
	 * Whether shift is currently pressed.
	 */
	private boolean aShiftPressed = false;

	/**
	 * Hidden constructor.
	 */
	private EVInputManager()
	{
	}

	/**
	 * Responds to all keyboard/mouse actions and passes the correct message down the everVoid view stack. Actions are one time
	 * events, liked KeyPressed or MouseClicked.
	 * 
	 * @param name
	 *            The name of the action.
	 * @param isPressed
	 *            Whether the key is currently pressed.
	 * @param tpf
	 *            The time per frame of the action.
	 * @param position
	 *            The location of the key on the frame.
	 */
	public void onAction(final String name, final boolean isPressed, final float tpf, final Vector2f position)
	{
		if (name.equals("Mouse left")) {
			// Forward left clicks to game view
			if (isPressed) {
				EVViewManager.onLeftClick(position, tpf);
			}
			else {
				EVViewManager.onLeftRelease(position, tpf);
			}
		}
		else if (name.equals("Mouse right")) {
			// Forward right clicks to game view
			if (isPressed) {
				EVViewManager.onRightClick(position, tpf);
			}
			else {
				EVViewManager.onRightRelease(position, tpf);
			}
		}
		final KeyboardKey key = KeyboardKey.fromMapping(name);
		if (key != null) {
			if (key.isShift()) {
				aShiftPressed = isPressed;
			}
			if (isPressed) {
				if (!EVViewManager.onKeyPress(key, tpf) && key.equals(KeyboardKey.ESCAPE)) {
					EverVoidClient.quit();
				}
			}
			else {
				EVViewManager.onKeyRelease(key, tpf);
			}
		}
	}

	/**
	 * Responds to all analog actions and passes the correct message down the everVoid view stack. Analog actions are long time
	 * events, like moving the mouse or scrolling.
	 * 
	 * @param name
	 *            The name of the action.
	 * @param delta
	 *            The value by how much the event moved.
	 * @param tpf
	 *            The time per frame of the action.
	 * @param position
	 *            The location of the key on the frame.
	 */
	public void onAnalog(final String name, final float delta, final float tpf, final Vector2f position)
	{
		if (name.equals("Mouse move")) {
			// Forward mouse movement to game view
			EVViewManager.onMouseMove(position, tpf);
		}
		else if (name.equals("Mouse wheel up")) {
			EVViewManager.onMouseWheelUp(delta, tpf, position);
		}
		else if (name.equals("Mouse wheel down")) {
			EVViewManager.onMouseWheelDown(delta, tpf, position);
		}
	}
}

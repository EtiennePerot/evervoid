package com.evervoid.client.interfaces;

import com.evervoid.client.KeyboardKey;
import com.jme3.math.Vector2f;

/**
 * Classes implementing the EVInputListener interface can receive mouse and keyboard input events.
 */
public interface EVInputListener
{
	/**
	 * Called when a keyboard key is pressed
	 * 
	 * @param key
	 *            The {@link KeyboardKey} being pressed
	 * @param tpf
	 *            The current time-per-frame (in seconds)
	 * @return Whether the object implementing EVInputListener has handled this event or not; if it has, the event will not be
	 *         propagated any further to other classes.
	 */
	public boolean onKeyPress(final KeyboardKey key, final float tpf);

	/**
	 * Called when a keyboard key is released
	 * 
	 * @param key
	 *            The {@link KeyboardKey} being released
	 * @param tpf
	 *            The current time-per-frame (in seconds)
	 * @return Whether the object implementing EVInputListener has handled this event or not; if it has, the event will not be
	 *         propagated any further to other classes.
	 */
	public boolean onKeyRelease(final KeyboardKey key, final float tpf);

	/**
	 * Called when the left mouse button is pressed
	 * 
	 * @param position
	 *            The mouse position at the moment of the click
	 * @param tpf
	 *            The current time-per-frame (in seconds)
	 * @return Whether the object implementing EVInputListener has handled this event or not; if it has, the event will not be
	 *         propagated any further to other classes.
	 */
	public boolean onLeftClick(final Vector2f position, final float tpf);

	/**
	 * Called when the left mouse button is released
	 * 
	 * @param position
	 *            The mouse position at the moment of the release
	 * @param tpf
	 *            The current time-per-frame (in seconds)
	 * @return Whether the object implementing EVInputListener has handled this event or not; if it has, the event will not be
	 *         propagated any further to other classes.
	 */
	public boolean onLeftRelease(final Vector2f position, final float tpf);

	/**
	 * Called when the mouse moves on the screen
	 * 
	 * @param position
	 *            The mouse position at the present time
	 * @param tpf
	 *            The current time-per-frame (in seconds)
	 * @return Whether the object implementing EVInputListener has handled this event or not; if it has, the event will not be
	 *         propagated any further to other classes.
	 */
	public boolean onMouseMove(final Vector2f position, final float tpf);

	/**
	 * Called when the mouse wheel is scrolled downwards
	 * 
	 * @param delta
	 *            A measurement of how much the wheel has moved down. Larger values mean larger scrolls.
	 * @param tpf
	 *            The current time-per-frame (in seconds)
	 * @param position
	 *            The mouse position at the moment of the scroll
	 * @return Whether the object implementing EVInputListener has handled this event or not; if it has, the event will not be
	 *         propagated any further to other classes.
	 */
	public boolean onMouseWheelDown(final float delta, final float tpf, final Vector2f position);

	/**
	 * Called when the mouse wheel is scrolled upwards
	 * 
	 * @param delta
	 *            A measurement of how much the wheel has moved up. Larger values mean larger scrolls.
	 * @param tpf
	 *            The current time-per-frame (in seconds)
	 * @param position
	 *            The mouse position at the moment of the scroll
	 * @return Whether the object implementing EVInputListener has handled this event or not; if it has, the event will not be
	 *         propagated any further to other classes.
	 */
	public boolean onMouseWheelUp(final float delta, final float tpf, final Vector2f position);

	/**
	 * Called when the right mouse button is pressed
	 * 
	 * @param position
	 *            The mouse position at the moment of the click
	 * @param tpf
	 *            The current time-per-frame (in seconds)
	 * @return Whether the object implementing EVInputListener has handled this event or not; if it has, the event will not be
	 *         propagated any further to other classes.
	 */
	public boolean onRightClick(final Vector2f position, final float tpf);

	/**
	 * Called when the right mouse button is released
	 * 
	 * @param position
	 *            The mouse position at the moment of the release
	 * @param tpf
	 *            The current time-per-frame (in seconds)
	 * @return Whether the object implementing EVInputListener has handled this event or not; if it has, the event will not be
	 *         propagated any further to other classes.
	 */
	public boolean onRightRelease(final Vector2f position, final float tpf);
}

package com.evervoid.client.graphics;

import com.jme3.math.Vector2f;

/**
 * Classes implementing the Sizable interface are aware of their 2-dimensional size, and can be queried from their width and
 * height.
 */
public interface Sizable
{
	/**
	 * @return A {@link Vector2f} containing the element's width as x, and height as y.
	 */
	public Vector2f getDimensions();

	/**
	 * @return The height of the element.
	 */
	public float getHeight();

	/**
	 * @return The width of the element.
	 */
	public float getWidth();
}

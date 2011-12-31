package com.evervoid.client.views;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.Vector2f;

/**
 * Represents 2-dimensional bounded region. Used for various things. It uses integers for geometry, whereas {@link Rectangle}
 * uses floating-point numbers.
 */
public class Bounds
{
	/**
	 * @return A {@link Bounds} instance representing the whole game window.
	 */
	public static Bounds getWholeScreenBounds()
	{
		final Dimension w = EverVoidClient.getWindowDimension();
		return new Bounds(0, 0, w.width, w.height);
	}

	/**
	 * @param margin
	 *            A margin to apply to each border, in pixels
	 * @return A {@link Bounds} instance representing the whole game window minus the given margin on each side of the window.
	 */
	public static Bounds getWholeScreenBounds(final int margin)
	{
		final Dimension w = EverVoidClient.getWindowDimension();
		return new Bounds(margin, margin, w.width - margin * 2, w.height - margin * 2);
	}

	/**
	 * The height of the bounded region
	 */
	public final int height;
	/**
	 * The width of the bounded region
	 */
	public final int width;
	/**
	 * The x offset of the bounded region from the origin
	 */
	public final int x;
	/**
	 * The y offset of the bounded region from the origin
	 */
	public final int y;

	/**
	 * Constructor
	 * 
	 * @param x
	 *            X offset of the bounded region
	 * @param y
	 *            Y offset of the bounded region
	 * @param width
	 *            Width of the bounded region
	 * @param height
	 *            Height of the bounded region
	 */
	public Bounds(final float x, final float y, final float width, final float height)
	{
		this((int) x, (int) y, (int) width, (int) height);
	}

	/**
	 * Constructor
	 * 
	 * @param x
	 *            X offset of the bounded region
	 * @param y
	 *            Y offset of the bounded region
	 * @param width
	 *            Width of the bounded region
	 * @param height
	 *            Height of the bounded region
	 */
	public Bounds(final int x, final int y, final int width, final int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * @param x
	 *            X offset relatively to this {@link Bounds} instance
	 * @param y
	 *            Y offset relatively to this {@link Bounds} instance
	 * @return A new {@link Bounds} instance, translated by the given offset relatively to this {@link Bounds} instance. Does
	 *         not modify the current instance.
	 */
	public Bounds add(final float x, final float y)
	{
		return new Bounds(this.x + x, this.y + y, width, height);
	}

	/**
	 * @param offset
	 *            Offset relatively to this {@link Bounds} instance
	 * @return A new {@link Bounds} instance, translated by the given offset relatively to this {@link Bounds} instance. Does
	 *         not modify the current instance.
	 */
	public Bounds add(final Vector2f offset)
	{
		return add(offset.x, offset.y);
	}

	@Override
	public Bounds clone()
	{
		return new Bounds(x, y, width, height);
	}

	/**
	 * @param x
	 *            X coordinate of the point
	 * @param y
	 *            Y coordinate of the point
	 * @return Whether the given point is contained within the bounded region (true) or not (false)
	 */
	public boolean contains(final float x, final float y)
	{
		return contains((int) x, (int) y);
	}

	/**
	 * @param x
	 *            X coordinate of the rectangle
	 * @param y
	 *            Y coordinate of the rectangle
	 * @param width
	 *            Width of the rectangle
	 * @param height
	 *            Height of the rectangle
	 * @return Whether the given rectangle is entirely contained within the bounded region (true) or not (false)
	 */
	public boolean contains(final float x, final float y, final float width, final float height)
	{
		return contains((int) x, (int) y, (int) width, (int) height);
	}

	/**
	 * @param x
	 *            X coordinate of the point
	 * @param y
	 *            Y coordinate of the point
	 * @return Whether the given point is contained within the bounded region (true) or not (false)
	 */
	public boolean contains(final int x, final int y)
	{
		return this.x <= x && x < this.x + width && this.y <= y && y < this.y + height;
	}

	/**
	 * @param x
	 *            X coordinate of the rectangle
	 * @param y
	 *            Y coordinate of the rectangle
	 * @param width
	 *            Width of the rectangle
	 * @param height
	 *            Height of the rectangle
	 * @return Whether the given rectangle is entirely contained within the bounded region (true) or not (false)
	 */
	public boolean contains(final int x, final int y, final int width, final int height)
	{
		return contains(x, y) && contains(x + width, y + height);
	}

	/**
	 * @param position
	 *            The point to check
	 * @return Whether the given point is contained within the bounded region (true) or not (false)
	 */
	public boolean contains(final Vector2f position)
	{
		return contains(position.x, position.y);
	}

	/**
	 * Returns a contracted version of the bounds by a certain percentage
	 * 
	 * @param percentage
	 *            The percentage to contract the bounds by (from 0 to 1)
	 * @return The contracted bounds
	 */
	public Bounds contract(final float percentage)
	{
		return new Bounds(x + width * percentage, y + height * percentage, width * (1f - 2f * percentage), height
				* (1f - 2f * percentage));
	}

	/**
	 * @return A {@link Rectangle} object with the same geometry as this {@link Bounds} instance.
	 */
	public Rectangle getRectangle()
	{
		return new Rectangle(x, y, width, height);
	}

	/**
	 * @return A new {@link Bounds} object with 0 as x and y, but same width and height
	 */
	public Bounds getZeroOrigin()
	{
		return new Bounds(0, 0, width, height);
	}

	@Override
	public String toString()
	{
		return "Bounds[" + x + "; " + y + " @ " + width + "x" + height + "]";
	}
}

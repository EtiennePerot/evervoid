package com.evervoid.client.views;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.Vector2f;

public class Bounds
{
	public static Bounds getWholeScreenBounds()
	{
		final Dimension w = EverVoidClient.getWindowDimension();
		return new Bounds(0, 0, w.width, w.height);
	}

	public static Bounds getWholeScreenBounds(final int margin)
	{
		final Dimension w = EverVoidClient.getWindowDimension();
		return new Bounds(margin, margin, w.width - margin * 2, w.height - margin * 2);
	}

	public final int height;
	public final int width;
	public final int x;
	public final int y;

	public Bounds(final float x, final float y, final float width, final float height)
	{
		this((int) x, (int) y, (int) width, (int) height);
	}

	public Bounds(final int x, final int y, final int width, final int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Bounds add(final Vector2f offset)
	{
		return new Bounds(x + offset.x, y + offset.y, width, height);
	}

	@Override
	public Bounds clone()
	{
		return new Bounds(x, y, width, height);
	}

	public boolean contains(final float x, final float y)
	{
		return contains((int) x, (int) y);
	}

	public boolean contains(final float x, final float y, final float width, final float height)
	{
		return contains((int) x, (int) y, (int) width, (int) height);
	}

	public boolean contains(final int x, final int y)
	{
		return this.x <= x && x < this.x + width && this.y <= y && y < this.y + height;
	}

	public boolean contains(final int x, final int y, final int width, final int height)
	{
		return contains(x, y) && contains(x + width, y + height);
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

	public Rectangle getRectangle()
	{
		return new Rectangle(x, y, width, height);
	}

	/**
	 * @return A new Bounds object with 0 as x and y, but same width and height
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

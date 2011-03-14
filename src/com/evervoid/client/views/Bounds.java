package com.evervoid.client.views;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.state.geometry.Dimension;

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

	@Override
	public Bounds clone()
	{
		return new Bounds(x, y, width, height);
	}

	public Rectangle getRectangle()
	{
		return new Rectangle(x, y, width, height);
	}

	@Override
	public String toString()
	{
		return "Bounds[" + x + "; " + y + " @ " + width + "x" + height + "]";
	}
}

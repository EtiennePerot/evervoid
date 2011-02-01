package com.evervoid.client.views;

import com.evervoid.client.graphics.geometry.Rectangle;

public class Bounds
{
	public final int height;
	public final int width;
	public final int x;
	public final int y;

	public Bounds(final int x, final int y, final int width, final int height)
	{
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
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

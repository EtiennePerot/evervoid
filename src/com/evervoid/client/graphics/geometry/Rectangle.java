package com.evervoid.client.graphics.geometry;

import com.jme3.math.Vector2f;

public class Rectangle
{
	public float height;
	public float width;
	public float x;
	public float y;

	public Rectangle(final float x, final float y, final float width, final float height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Vector2f getBottomLeft()
	{
		return new Vector2f(x, y);
	}

	public Vector2f getBottomRight()
	{
		return new Vector2f(x + width, y);
	}

	public Vector2f getTopLeft()
	{
		return new Vector2f(x, y + height);
	}

	public Vector2f getTopRight()
	{
		return new Vector2f(x + width, y + height);
	}

	@Override
	public String toString()
	{
		return "Rect[" + x + ", " + y + " @ " + width + "x" + height + "]";
	}
}

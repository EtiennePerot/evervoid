package com.evervoid.client.graphics.geometry;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

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

	public Rectangle add(final float x, final float y, final float width, final float height)
	{
		return new Rectangle(this.x + x, this.y + y, this.width + width, this.height + height);
	}

	public Vector2f getBottomLeft()
	{
		return new Vector2f(x, y);
	}

	public Vector2f getBottomRight()
	{
		return new Vector2f(x + width, y);
	}

	public Vector2f getCenter2f()
	{
		return new Vector2f(x + width / 2, y + height / 2);
	}

	public Vector3f getCenter3f()
	{
		return new Vector3f(x + width / 2, y + height / 2, 0);
	}

	public Vector2f getClosestTo(final Vector2f target)
	{
		final float bestX = MathUtils.clampFloat(x, target.x, x + width);
		final float bestY = MathUtils.clampFloat(y, target.y, y + height);
		return new Vector2f(bestX, bestY);
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

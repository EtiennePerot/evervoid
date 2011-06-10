package com.evervoid.client.graphics.geometry;

import com.evervoid.client.views.Bounds;
import com.evervoid.utils.MathUtils;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * A rectangle shape that uses floats for bounding, as opposed to {@link Bounds} which uses ints. Useful for geometry rectangles
 */
public class Rectangle
{
	/**
	 * The height of the rectangle
	 */
	public float height;
	/**
	 * The width of the rectangle
	 */
	public float width;
	/**
	 * The X coordinate of the lower-left corner of the rectangle
	 */
	public float x;
	/**
	 * The Y coordinate of the lower-left corner of the rectangle
	 */
	public float y;

	/**
	 * Constructor
	 * 
	 * @param x
	 *            The X coordinate of the lower-left corner of the rectangle
	 * @param y
	 *            The Y coordinate of the lower-left corner of the rectangle
	 * @param width
	 *            The width of the rectangle
	 * @param height
	 *            The height of the rectangle
	 */
	public Rectangle(final float x, final float y, final float width, final float height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Constructor
	 * 
	 * @param origin
	 *            The lower-left corner of the rectangle, as a {@link Vector2f}
	 * @param width
	 *            The width of the rectangle
	 * @param height
	 *            The height of the rectangle
	 */
	public Rectangle(final Vector2f origin, final float width, final float height)
	{
		this(origin.x, origin.y, width, height);
	}

	/**
	 * Returns a new {@link Rectangle} instance with offsets on all its components
	 * 
	 * @param x
	 *            Add this offset to the X coordinate of the rectangle
	 * @param y
	 *            Add this offset to the Y coordinate of the rectangle
	 * @param width
	 *            Add this much width to the rectangle
	 * @param height
	 *            Add this much height to the rectangle
	 * @return The new rectangle
	 */
	public Rectangle add(final float x, final float y, final float width, final float height)
	{
		return new Rectangle(this.x + x, this.y + y, this.width + width, this.height + height);
	}

	/**
	 * Builds a new rectangle with the same dimensions, but another (x, y) lower-left corner
	 * 
	 * @param offset
	 *            The offset to add to the current lower-left corner
	 * @return The new rectangle
	 */
	public Rectangle add(final Vector2f offset)
	{
		return new Rectangle(x + offset.x, y + offset.y, width + width, height + height);
	}

	/**
	 * @return The coordinates of the lower-left corner of the rectangle, as a {@link Vector2f}
	 */
	public Vector2f getBottomLeft()
	{
		return new Vector2f(x, y);
	}

	/**
	 * @return The coordinates of the lower-right corner of the rectangle, as a {@link Vector2f}
	 */
	public Vector2f getBottomRight()
	{
		return new Vector2f(x + width, y);
	}

	/**
	 * @return The coordinates of the center of the rectangle, as a {@link Vector2f}
	 */
	public Vector2f getCenter2f()
	{
		return new Vector2f(x + width / 2, y + height / 2);
	}

	/**
	 * @return The coordinates of the lower-left corner of the rectangle, as a {@link Vector3f} (Z is 0)
	 */
	public Vector3f getCenter3f()
	{
		return new Vector3f(x + width / 2, y + height / 2, 0);
	}

	/**
	 * Finds the point inside the rectangle that is as close as possible to the given target
	 * 
	 * @param target
	 *            The target {@link Vector2f} to find the closest point to
	 * @return The coordinates of the closest point
	 */
	public Vector2f getClosestTo(final Vector2f target)
	{
		final float bestX = MathUtils.clampFloat(x, target.x, x + width);
		final float bestY = MathUtils.clampFloat(y, target.y, y + height);
		return new Vector2f(bestX, bestY);
	}

	/**
	 * @return The coordinates of a random point inside the rectangle
	 */
	public Vector2f getRandomVector()
	{
		return new Vector2f(MathUtils.getRandomFloatBetween(x, x + width), MathUtils.getRandomFloatBetween(y, y + height));
	}

	/**
	 * @return The coordinates of the top-left corner of the rectangle, as a {@link Vector2f}
	 */
	public Vector2f getTopLeft()
	{
		return new Vector2f(x, y + height);
	}

	/**
	 * @return The coordinates of the top-right corner of the rectangle, as a {@link Vector2f}
	 */
	public Vector2f getTopRight()
	{
		return new Vector2f(x + width, y + height);
	}

	/**
	 * Checks if a given point is close to the middle of the rectangle. "Close" is defined as
	 * "not being more than half the length of the average dimension (width/height) away"
	 * 
	 * @param target
	 *            The point to check the closeness of
	 * @return Whether the given point is close to the middle or not
	 */
	public boolean isCloseToMiddle(final Vector2f target)
	{
		return getCenter2f().distance(target) <= (width + height) / 4f;
	}

	/**
	 * Multiplies all parameters of this rectangle by the given scalar, including the (x, y) position.
	 * 
	 * @param scale
	 *            The scale to multiply by
	 * @return The new rectangle
	 */
	public Rectangle mult(final float scale)
	{
		return new Rectangle(x * scale, y * scale, width * scale, height * scale);
	}

	@Override
	public String toString()
	{
		return "Rect[" + x + ", " + y + " @ " + width + "x" + height + "]";
	}
}

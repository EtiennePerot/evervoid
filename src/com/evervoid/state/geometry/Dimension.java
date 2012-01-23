package com.evervoid.state.geometry;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.client.views.solar.SolarGrid;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;

/**
 * Dimension is a simple class representing the size of a rectangular object. The {@link GridLocation} class sets a Dimension to
 * a certain point, thus representing the location and size of a {@link Prop} inside a {@link SolarGrid}. Dimensions are used to
 * enforce postconditions on functions, such as the size of GridLocations needed in calls to determine the possible move
 * locations of a {@link Ship}.
 */
public class Dimension implements Cloneable, Jsonable
{
	/**
	 * THe Dimmension's height.
	 */
	public final int height;
	/**
	 * The dimmension's width.
	 */
	public final int width;

	/**
	 * Default constructor is equivalent to Dimension(1, 1)
	 */
	public Dimension()
	{
		this(1, 1);
	}

	/**
	 * Initializes a new Dimension object
	 * 
	 * @param width
	 *            The object's width
	 * @param height
	 *            The object's height
	 */
	public Dimension(final float width, final float height)
	{
		this((int) width, (int) height);
	}

	/**
	 * Initializes a new Dimension object
	 * 
	 * @param width
	 *            The object's width
	 * @param height
	 *            The object's height
	 */
	public Dimension(final int width, final int height)
	{
		this.width = width;
		this.height = height;
	}

	/**
	 * Casts a java.awt.Dimension
	 * 
	 * @param awtDimension
	 *            The dimension to cast
	 */
	public Dimension(final java.awt.Dimension awtDimension)
	{
		width = (int) awtDimension.getWidth();
		height = (int) awtDimension.getHeight();
	}

	/**
	 * Builds a new Dimension object from a Json object. The Json object may either be [w, h] or {width: width, height: height}.
	 * 
	 * @param j
	 *            The Json object to build from
	 */
	public Dimension(final Json j)
	{
		this(j.getIntAttribute("width"), j.getIntAttribute("height"));
	}

	@Override
	public Dimension clone()
	{
		return new Dimension(width, height);
	}

	/**
	 * @return True if the other object is a Dimension of the same size, false otherwise.
	 */
	@Override
	public boolean equals(final Object other)
	{
		if (super.equals(other)) {
			return true;
		}
		if (other == null || !other.getClass().equals(getClass())) {
			return false;
		}
		final Dimension d = (Dimension) other;
		return width == d.width && height == d.height;
	}

	/**
	 * @return The average of the width and the height.
	 */
	public int getAverageSize()
	{
		return (width + height) / 2;
	}

	/**
	 * @return The object's height, truncated to the nearest integer.
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * @return The object's height.
	 */
	public float getHeightFloat()
	{
		return height;
	}

	/**
	 * Return the Set of Points spanned from the given point by this Dimension
	 * 
	 * @param point
	 *            The origin point
	 * @return The Set of Points spanned
	 */
	public Set<Point> getPoints(final Point point)
	{
		final Set<Point> points = new HashSet<Point>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				points.add(point.add(x, y));
			}
		}
		return points;
	}

	/**
	 * @return The object's width.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * @return The object's width as a float
	 */
	public float getWidthFloat()
	{
		return width;
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	/**
	 * Dimension comparison using specified width and height.
	 * 
	 * @param width
	 *            Width of the item to compare to.
	 * @param height
	 *            Height of the item to compare to.
	 * @return True if the dimension has the same width and height, false otherwise.
	 */
	public boolean sameAs(final int width, final int height)
	{
		return this.width == width && this.height == height;
	}

	/**
	 * Note: the value is cast to a float, this is just a wrapper of scale(float scale) for ease of use.
	 * 
	 * @param scale
	 *            The amount by which to scale the dimension.
	 * @return A new dimension scaled by the given value.
	 */
	public Dimension scale(final double scale)
	{
		return scale((float) scale);
	}

	/**
	 * @param scale
	 *            The amount by which to scale the dimension.
	 * @return A new dimension scaled by the given value.
	 */
	public Dimension scale(final float scale)
	{
		return new Dimension(width * scale, height * scale);
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("width", width).setAttribute("height", height);
	}

	@Override
	public String toString()
	{
		return "[" + width + "x" + height + "]";
	}
}

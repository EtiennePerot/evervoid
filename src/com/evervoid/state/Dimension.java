package com.evervoid.state;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class Dimension implements Jsonable
{
	public final int height;
	public final int width;

	/**
	 * Default constructor is equivalent to Dimension(1, 1)
	 */
	public Dimension()
	{
		this(1, 1);
	}

	/**
	 * Initialises a new Dimension object
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

	public Dimension(final java.awt.Dimension awtDimension)
	{
		width = (int) awtDimension.getWidth();
		height = (int) awtDimension.getHeight();
	}

	public Dimension(final Json j)
	{
		this(j.getIntAttribute("width"), j.getIntAttribute("height"));
	}

	/**
	 * @param other
	 *            Other Object to compare to.
	 * @return True if the other object is a Dimension of the same size, false otherwise.
	 */
	@Override
	public boolean equals(final Object other)
	{
		if (super.equals(other)) {
			return true;
		}
		if (!other.getClass().equals(getClass())) {
			return false;
		}
		final Dimension d = (Dimension) other;
		return width == d.width && height == d.height;
	}

	/**
	 * @return Average ((width + height) / 2)
	 */
	public int getAverageSize()
	{
		return (width + height) / 2;
	}

	/**
	 * @return The object's height
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * @return The object's height as a float
	 */
	public float getHeightFloat()
	{
		return height;
	}

	/**
	 * @return The object's width
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

	@Override
	public Json toJson()
	{
		return new Json().setIntAttribute("width", width).setIntAttribute("height", height);
	}

	@Override
	public String toString()
	{
		return "[" + width + "x" + height + "]";
	}
}

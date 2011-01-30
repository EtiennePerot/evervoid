package com.evervoid.state;

public class Dimension
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
	 * @param other
	 *            Other dimension to compare to.
	 * @return True if the other dimension is the same, false otherwise.
	 */
	public boolean sameAs(final Dimension other)
	{
		return width == other.width && height == other.height;
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
	public String toString()
	{
		return "[" + width + "x" + height + "]";
	}
}

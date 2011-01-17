package com.evervoid.gamedata;

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
	 * @return The object's height
	 */
	public int getHeight()
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

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	@Override
	public String toString()
	{
		return "[" + width + "x" + height + "]";
	}
}

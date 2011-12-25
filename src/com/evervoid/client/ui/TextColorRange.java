package com.evervoid.client.ui;

import com.jme3.math.ColorRGBA;

/**
 * Data structure that holds a text range that should have a special color in a BaseText.
 */
public class TextColorRange
{
	/**
	 * The {@link ColorRGBA} of the text
	 */
	public final ColorRGBA color;
	/**
	 * The end index of the range
	 */
	public final int end;
	/**
	 * The beginning index of the range
	 */
	public final int start;

	/**
	 * Constructor
	 * 
	 * @param start
	 *            The beginning index of the range
	 * @param end
	 *            The end index of the range
	 * @param color
	 *            The {@link ColorRGBA} of the text
	 */
	public TextColorRange(final int start, final int end, final ColorRGBA color)
	{
		this.start = start;
		this.end = end;
		this.color = color;
	}

	/**
	 * Get the color of the text of this range with a specific alpha multiplier
	 * 
	 * @param alpha
	 *            The alpha multiplier
	 * @return The {@link ColorRGBA} multiplied with the given alpha multiplier
	 */
	public ColorRGBA getAtAlpha(final float alpha)
	{
		return new ColorRGBA(color.r, color.g, color.b, color.a * alpha);
	}
}

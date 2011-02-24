package com.evervoid.client.ui;

import com.jme3.math.ColorRGBA;

/**
 * Data structure that holds a text range that should have a special color in a BaseText.
 */
public class TextColorRange
{
	public final ColorRGBA color;
	// These are final, they can be public
	public final int end;
	public final int start;

	public TextColorRange(final int start, final int end, final ColorRGBA color)
	{
		this.start = start;
		this.end = end;
		this.color = color;
	}

	public ColorRGBA getAtAlpha(final float alpha)
	{
		return new ColorRGBA(color.r, color.g, color.b, color.a * alpha);
	}
}

package com.evervoid.client.graphics;

import com.evervoid.state.Color;
import com.jme3.math.ColorRGBA;

/**
 * Graphic-related utility functions
 */
public class GraphicsUtils
{
	/**
	 * Convert a {@link Color} (Game state representation of color) to a {@link ColorRGBA} (jMonkeyEngine3's representation of
	 * color)
	 * 
	 * @param color
	 *            The {@link Color} to convert
	 * @return The converted {@link ColorRGBA}
	 */
	public static ColorRGBA getColorRGBA(final Color color)
	{
		return new ColorRGBA(color.red, color.green, color.blue, color.alpha);
	}
}

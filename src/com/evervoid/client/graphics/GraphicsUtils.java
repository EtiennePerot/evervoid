package com.evervoid.client.graphics;

import com.evervoid.state.Color;
import com.jme3.math.ColorRGBA;

public class GraphicsUtils
{
	public static ColorRGBA getColorRGBA(final Color color)
	{
		return new ColorRGBA(color.red, color.green, color.blue, color.alpha);
	}
}

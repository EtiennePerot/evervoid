package com.evervoid.client.graphics;

import com.evervoid.state.player.PlayerColor;
import com.jme3.math.ColorRGBA;

public class GraphicsUtils
{
	public static ColorRGBA getPlayerColor(final PlayerColor color)
	{
		return new ColorRGBA(color.red, color.green, color.blue, color.alpha);
	}
}

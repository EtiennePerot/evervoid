package com.evervoid.client.graphics;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;

public class BaseText extends BitmapText
{
	public BaseText(final String font, final String text, final ColorRGBA color)
	{
		super(GraphicManager.getFont(font));
		setText(text);
		setColor(color);
	}
}

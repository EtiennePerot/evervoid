package com.evervoid.client.ui;

import com.jme3.math.ColorRGBA;

/**
 * A Button control.
 */
public class ButtonControl extends BorderedControl
{
	private static ColorRGBA sButtonTextColor = new ColorRGBA(0.9f, 0.9f, 0.9f, 1f);

	public ButtonControl(final String label)
	{
		super("ui/button_left.png", new CenteredBackgroundedControl(new StaticTextControl(label, sButtonTextColor),
				"ui/button_middle.png"), "ui/button_right.png");
	}
}

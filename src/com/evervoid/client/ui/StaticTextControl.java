package com.evervoid.client.ui;

import com.evervoid.state.geometry.Dimension;
import com.jme3.math.ColorRGBA;

public class StaticTextControl extends UIControl
{
	private final Dimension aDimension;

	public StaticTextControl(final String text, final ColorRGBA color)
	{
		final BaseText textLabel = new BaseText(text, color);
		aDimension = new Dimension((int) textLabel.getWidth(), (int) textLabel.getHeight());
		System.out.println("Text dimension is " + aDimension);
		addNode(textLabel);
	}

	@Override
	public Dimension getMinimumSize()
	{
		// FIXME: Hax while waiting for jME3 to fix this bug:
		// http://jmonkeyengine.org/groups/gui/forum/topic/bitmaptext-getlinewidth-always-returns-0/
		return new Dimension(aDimension.width + 256, aDimension.height);
	}
}

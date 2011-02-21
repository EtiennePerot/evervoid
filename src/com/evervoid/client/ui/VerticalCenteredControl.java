package com.evervoid.client.ui;

/**
 * Wraps a UIControl to make it vertically centered
 */
public class VerticalCenteredControl extends WrapperControl
{
	public VerticalCenteredControl(final Resizeable contained)
	{
		super(contained, BoxDirection.VERTICAL);
		addChildUI(new UIControl(), 1);
		addChildUI(contained, 0);
		addChildUI(new UIControl(), 1);
	}
}

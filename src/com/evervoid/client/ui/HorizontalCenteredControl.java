package com.evervoid.client.ui;

/**
 * Wraps a UIControl to make it horizontally centered
 */
public class HorizontalCenteredControl extends WrapperControl
{
	public HorizontalCenteredControl(final Resizeable contained)
	{
		super(contained, BoxDirection.HORIZONTAL);
		addChildUI(new UIControl(), 1);
		addChildUI(contained, 0);
		addChildUI(new UIControl(), 1);
	}
}

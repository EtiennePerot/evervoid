package com.evervoid.client.ui;

/**
 * Wraps a UIControl to make it horizontally centered
 */
public class HorizontalCenteredControl extends WrapperControl
{
	public HorizontalCenteredControl(final Resizeable contained)
	{
		super(contained, BoxDirection.HORIZONTAL);
		addUI(new UIControl(), 1);
		addUI(contained, 0);
		addUI(new UIControl(), 1);
	}
}

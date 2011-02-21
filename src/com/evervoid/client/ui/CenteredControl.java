package com.evervoid.client.ui;

/**
 * Wraps a UIControl to make it centered in all directions
 */
public class CenteredControl extends WrapperControl
{
	public CenteredControl(final Resizeable contained)
	{
		super(contained, BoxDirection.VERTICAL);
		addUI(new UIControl(), 1);
		addUI(new HorizontalCenteredControl(contained), 0);
		addUI(new UIControl(), 1);
	}
}

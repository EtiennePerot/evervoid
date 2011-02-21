package com.evervoid.client.ui;

import com.evervoid.client.views.Bounds;

/**
 * Wraps a UIControl to make it horizontally centered
 */
public class HorizontalCenteredControl extends UIControl
{
	public HorizontalCenteredControl(final Resizeable contained)
	{
		super(BoxDirection.HORIZONTAL);
		addUI(new UIControl(), 1);
		addUI(contained, 0);
		addUI(new UIControl(), 1);
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
	}
}

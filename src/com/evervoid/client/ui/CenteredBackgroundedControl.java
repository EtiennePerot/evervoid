package com.evervoid.client.ui;

public class CenteredBackgroundedControl extends BackgroundedUIControl implements Resizeable
{
	public CenteredBackgroundedControl(final Resizeable control, final String background)
	{
		super(BoxDirection.HORIZONTAL, background);
		addUI(new CenteredControl(control), 1);
	}
}

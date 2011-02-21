package com.evervoid.client.ui;

/**
 * Combines centering with Backgrounding. This is a centered (in both directions) and backgrounded UIControl.
 */
public class CenteredBackgroundedControl extends BackgroundedUIControl
{
	public CenteredBackgroundedControl(final Resizeable contained, final String background)
	{
		super(new CenteredControl(contained), background);
	}
}

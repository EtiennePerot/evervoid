package com.evervoid.client.ui;

public class CenteredBackgroundedControl extends BackgroundedUIControl implements Resizeable
{
	private final Resizeable aContained;

	public CenteredBackgroundedControl(final Resizeable control, final String background)
	{
		super(BoxDirection.HORIZONTAL, background);
		aContained = control;
		addChildUI(new CenteredControl(aContained), 1);
	}

	@Override
	public void addUI(final Resizeable control, final int spring)
	{
		if (aContained instanceof UIControl) {
			((UIControl) aContained).addUI(control, spring);
		}
	}
}

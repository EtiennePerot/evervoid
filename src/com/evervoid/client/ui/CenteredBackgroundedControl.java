package com.evervoid.client.ui;

public class CenteredBackgroundedControl extends BackgroundedUIControl implements Resizeable
{
	private final Resizeable aContained;

	public CenteredBackgroundedControl(final Resizeable control, final String background)
	{
		super(BoxDirection.HORIZONTAL, background);
		aContained = control;
		addUI(new CenteredControl(aContained), 1);
	}

	@Override
	public void addSubUI(final Resizeable control, final int spring)
	{
		if (aContained instanceof UIControl) {
			((UIControl) aContained).addSubUI(control, spring);
		}
	}
}

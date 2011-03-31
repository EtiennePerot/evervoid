package com.evervoid.client.ui;

public class CenteredBackgroundedControl extends BackgroundedUIControl
{
	private final UIControl aContained;

	public CenteredBackgroundedControl(final UIControl control, final String background)
	{
		super(BoxDirection.HORIZONTAL, background);
		aContained = control;
		addChildUI(new CenteredControl(aContained), 1);
	}

	@Override
	public UIControl addUI(final UIControl control)
	{
		aContained.addUI(control);
		return this;
	}

	@Override
	public void addUI(final UIControl control, final int spring)
	{
		aContained.addUI(control, spring);
	}
}

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
		return aContained.addUI(control);
	}

	@Override
	public UIControl addUI(final UIControl control, final int spring)
	{
		return aContained.addUI(control, spring);
	}
}

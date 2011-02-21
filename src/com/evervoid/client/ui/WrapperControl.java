package com.evervoid.client.ui;

abstract class WrapperControl extends UIControl
{
	protected final Resizeable aContained;

	WrapperControl(final Resizeable contained, final BoxDirection direction)
	{
		super(direction);
		aContained = contained;
	}

	@Override
	public void addUI(final Resizeable control, final int spring)
	{
		if (aContained instanceof UIControl) {
			((UIControl) aContained).addUI(control, spring);
		}
	}
}

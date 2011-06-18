package com.evervoid.client.ui;

abstract class WrapperControl extends UIControl
{
	protected final UIControl aContained;

	WrapperControl(final UIControl contained)
	{
		this(contained, BoxDirection.HORIZONTAL);
	}

	WrapperControl(final UIControl contained, final BoxDirection direction)
	{
		super(direction);
		aContained = contained;
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

package com.evervoid.client.ui;

/**
 * A base abstract class that all {@link UIControl} subclasses wrapping elements within themselves should subclass (rather than
 * subclassing {@link UIControl} directly)
 */
abstract class WrapperControl extends UIControl
{
	/**
	 * The wrapped {@link UIControl}
	 */
	protected final UIControl aContained;

	/**
	 * Constructor
	 * 
	 * @param contained
	 *            The wrapped {@link UIControl}
	 */
	WrapperControl(final UIControl contained)
	{
		this(contained, BoxDirection.HORIZONTAL);
	}

	/**
	 * Constructor
	 * 
	 * @param contained
	 *            The wrapped {@link UIControl}
	 * @param direction
	 *            The {@link UIControl.BoxDirection} of this {@link WrapperControl}; usually not important
	 */
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

	@Override
	public void delAllChildUIs()
	{
		aContained.delAllChildUIs();
	}
}

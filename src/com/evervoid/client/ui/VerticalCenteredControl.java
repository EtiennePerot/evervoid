package com.evervoid.client.ui;

/**
 * Wraps a UIControl to make it vertically centered
 */
public class VerticalCenteredControl extends WrapperControl
{
	/**
	 * Constructor
	 * 
	 * @param contained
	 *            The {@link UIControl} to wrap
	 */
	public VerticalCenteredControl(final UIControl contained)
	{
		super(contained, BoxDirection.VERTICAL);
		addChildUI(new UIControl(), 1);
		addChildUI(contained, 0);
		addChildUI(new UIControl(), 1);
	}
}

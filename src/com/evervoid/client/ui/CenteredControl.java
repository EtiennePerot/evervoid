package com.evervoid.client.ui;

/**
 * Wraps a UIControl to make it centered in all directions
 */
public class CenteredControl extends WrapperControl
{
	/**
	 * Constructor
	 * 
	 * @param contained
	 *            The wrapped {@link UIControl}
	 */
	public CenteredControl(final UIControl contained)
	{
		super(contained, BoxDirection.VERTICAL);
		addChildUI(new UIControl(), 1);
		addChildUI(new HorizontalCenteredControl(contained), 0);
		addChildUI(new UIControl(), 1);
	}
}

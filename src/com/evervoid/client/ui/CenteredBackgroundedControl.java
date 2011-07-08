package com.evervoid.client.ui;

/**
 * Combines {@link CenteredControl} and {@link BackgroundedUIControl}: A {@link UIControl} that si both cenered and has a
 * background.
 */
public class CenteredBackgroundedControl extends BackgroundedUIControl
{
	/**
	 * The contained {@link UIControl}
	 */
	private final UIControl aContained;

	/**
	 * Constructor
	 * 
	 * @param control
	 *            The {@link UIControl} to wrap
	 * @param background
	 *            The background to use
	 */
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

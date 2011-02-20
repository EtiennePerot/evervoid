package com.evervoid.client.ui;

import com.evervoid.client.ui.Sizer.SizerDirection;

/**
 * Same as UIControl, but with a border on the left and right
 */
public class UIBorderedControl extends UIControl
{
	private final UIControl aContainer;
	private final UIConnector aLeftBorder;
	private final UIConnector aRightBorder;

	public UIBorderedControl(final SizerDirection direction, final String leftSprite, final String rightSprite)
	{
		setDirection(SizerDirection.HORIZONTAL);
		aLeftBorder = new UIConnector(leftSprite);
		aRightBorder = new UIConnector(leftSprite);
		aContainer = new UIControl();
		aContainer.setDirection(direction);
		// super.addControl(aLeftBorder);
		// super.addControl(aContainer, 1);
		// super.addControl(aRightBorder);
	}

	@Override
	public void addControl(final UIControl control, final int spring)
	{
		aContainer.addControl(control, spring);
	}
}

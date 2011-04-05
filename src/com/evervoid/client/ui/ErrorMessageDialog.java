package com.evervoid.client.ui;

import com.jme3.math.ColorRGBA;

public class ErrorMessageDialog extends UIControl implements ButtonListener
{
	private static final ColorRGBA sErrorMessageColor = new ColorRGBA(1f, 0.7f, 0.75f, 1f);
	private final Runnable aCallback;

	public ErrorMessageDialog(final String errorMessage, final Runnable callback)
	{
		final CenteredControl root = new CenteredControl(new UIControl());
		aCallback = callback;
		final PanelControl panel = new PanelControl("Error");
		panel.addString(errorMessage, sErrorMessageColor);
		panel.addSpacer(1, 8);
		final UIControl buttonContainer = new UIControl(BoxDirection.HORIZONTAL);
		buttonContainer.addFlexSpacer(1);
		final ButtonControl okButton = new ButtonControl("OK");
		okButton.addButtonListener(this);
		buttonContainer.addUI(okButton);
		panel.addUI(buttonContainer);
		root.addUI(panel);
		addUI(root, 1);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		if (aCallback != null) {
			aCallback.run();
		}
	}
}

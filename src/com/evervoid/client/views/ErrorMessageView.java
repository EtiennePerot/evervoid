package com.evervoid.client.views;

import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.jme3.math.ColorRGBA;

public class ErrorMessageView extends EverUIView implements ButtonListener
{
	private static final ColorRGBA sErrorMessageColor = new ColorRGBA(1f, 0.7f, 0.75f, 1f);
	private final Runnable aCallback;

	public ErrorMessageView(final String errorMessage, final Runnable callback)
	{
		super(new CenteredControl(new UIControl()));
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
		addUI(panel, 1);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		aCallback.run();
	}
}

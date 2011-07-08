package com.evervoid.client.ui;

import com.evervoid.utils.EVUtils;
import com.jme3.math.ColorRGBA;

/**
 * A dialog box that is meant to display an error message. Has a button to close it.
 */
public class ErrorMessageDialog extends UIControl implements ButtonListener
{
	/**
	 * The color of the text displaying the error string
	 */
	private static final ColorRGBA sErrorMessageColor = new ColorRGBA(1f, 0.7f, 0.75f, 1f);
	/**
	 * An optional {@link Runnable} that will be executed when the dialog is closed
	 */
	private final Runnable aCallback;

	/**
	 * Constructor
	 * 
	 * @param errorMessage
	 *            The error string to display
	 * @param callback
	 *            A {@link Runnable} that will be executed when the dialog is closed, or null if there is no need for it
	 */
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
		EVUtils.runCallback(aCallback);
	}
}

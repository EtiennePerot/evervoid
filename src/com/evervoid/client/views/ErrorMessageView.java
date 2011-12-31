package com.evervoid.client.views;

import com.evervoid.client.ui.ErrorMessageDialog;

/**
 * The ErrorMessageView is used to display an error message to the user. It uses the {@link ErrorMessageDialog} class from the
 * UI library, and wraps it into an {@link EverView} so that it can be shown in a {@link ComposedView} or exclusively.
 */
public class ErrorMessageView extends EverUIView
{
	/**
	 * Constructor
	 * 
	 * @param errorMessage
	 *            The error message to display
	 * @param callback
	 *            An optional callback {@link Runnable} that will be called when the user closes the error dialog. Set to null
	 *            if you don't want a callback.
	 */
	public ErrorMessageView(final String errorMessage, final Runnable callback)
	{
		super(new ErrorMessageDialog(errorMessage, callback));
	}
}

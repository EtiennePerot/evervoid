package com.evervoid.client.views;

import com.evervoid.client.ui.ErrorMessageDialog;

public class ErrorMessageView extends EverUIView
{
	public ErrorMessageView(final String errorMessage, final Runnable callback)
	{
		super(new ErrorMessageDialog(errorMessage, callback));
	}
}

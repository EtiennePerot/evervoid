package com.evervoid.client.ui;

/**
 * Classes implementing the ButtonListener interface will receive events from buttons.
 */
public interface ButtonListener
{
	/**
	 * Called when a buttons is clicked.
	 * 
	 * @param button
	 *            The buttons that was clicked (not necessarily a ButtonControl!).
	 */
	public void buttonClicked(final UIControl button);
}

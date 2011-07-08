package com.evervoid.client.ui;

/**
 * Classes implementing the ClickObserver interface will receive events whenever a {@link UIControl} is clicked.
 */
public interface ClickObserver
{
	/**
	 * Called when a {@link UIControl} is clicked
	 * 
	 * @param clicked
	 *            The {@link UIControl} that was clicked
	 */
	public void uiClicked(UIControl clicked);
}

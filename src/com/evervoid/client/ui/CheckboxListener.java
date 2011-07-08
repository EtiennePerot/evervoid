package com.evervoid.client.ui;

/**
 * Classes implementing the CheckboxListener interface will receive events from {@link CheckboxControl}s.
 */
public interface CheckboxListener
{
	/**
	 * Called when the {@link CheckboxControl} is checked or unchecked
	 * 
	 * @param checkbox
	 *            The {@link CheckboxControl} that was checked or unchecked
	 * @param checked
	 *            Whether the {@link CheckboxControl} is currently checked (true) or not (false)
	 */
	public void checkboxChecked(CheckboxControl checkbox, boolean checked);
}

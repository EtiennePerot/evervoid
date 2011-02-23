package com.evervoid.client.ui;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.client.KeyboardKey;

public class CheckboxControl extends UIConnector implements UIFocusable
{
	private boolean aCheckable = true;
	private boolean aChecked = false;
	private final String aCheckedSprite;
	private final Set<CheckboxListener> aListeners = new HashSet<CheckboxListener>();
	private final String aUncheckedSprite;

	public CheckboxControl(final String checked, final String unchecked)
	{
		super(unchecked);
		aUncheckedSprite = unchecked;
		aCheckedSprite = checked;
	}

	public void addListener(final CheckboxListener listener)
	{
		aListeners.add(listener);
	}

	@Override
	public void defocus()
	{
		setFocusedNode(null);
	}

	@Override
	public void focus()
	{
		if (aCheckable) {
			setChecked(!aChecked);
		}
		defocus(); // Can't stay focused on a checkbox
	}

	public boolean isChecked()
	{
		return aChecked;
	}

	@Override
	public void onKeyPress(final KeyboardKey key)
	{
		// Do nothing
	}

	@Override
	public void onKeyRelease(final KeyboardKey key)
	{
		// Do nothing
	}

	public void setCheckable(final boolean focusable)
	{
		aCheckable = focusable;
	}

	public void setChecked(final boolean checked)
	{
		aChecked = checked;
		if (aChecked) {
			setSprite(aCheckedSprite);
		}
		else {
			setSprite(aUncheckedSprite);
		}
		for (final CheckboxListener listener : aListeners) {
			listener.checkboxChecked(this, aChecked);
		}
	}
}

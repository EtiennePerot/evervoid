package com.evervoid.client.ui;

import java.util.HashSet;
import java.util.Set;

public class CheckboxControl extends ImageControl implements UIInputListener
{
	private boolean aCheckable = true;
	private boolean aChecked = false;
	private String aCheckedSprite;
	private final Set<CheckboxListener> aListeners = new HashSet<CheckboxListener>();
	private String aUncheckedSprite;

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

	public boolean isChecked()
	{
		return aChecked;
	}

	@Override
	public void onClick()
	{
		if (!isEnabled()) {
			return;
		}
		if (aCheckable) {
			setChecked(!aChecked);
		}
		onDefocus(); // Can't stay focused on a checkbox
	}

	@Override
	public void onDefocus()
	{
		setFocusedNode(null);
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

	public void setSprites(final String checked, final String unchecked)
	{
		final boolean refresh = (aChecked && !aCheckedSprite.equals(checked))
				|| (!aChecked && !aUncheckedSprite.equals(unchecked));
		aCheckedSprite = checked;
		aUncheckedSprite = unchecked;
		if (refresh) {
			setChecked(aChecked); // Refresh sprite
		}
	}
}

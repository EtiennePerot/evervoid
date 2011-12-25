package com.evervoid.client.ui;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.client.KeyboardKey;

/**
 * A clickable checkbox.
 */
public class CheckboxControl extends ImageControl implements UIInputListener
{
	/**
	 * Is the checkbox checkable (can its state change)?
	 */
	private boolean aCheckable = true;
	/**
	 * Is the checkbox checked?
	 */
	private boolean aChecked = false;
	/**
	 * Sprite to use when the checkbox is checked
	 */
	private String aCheckedSprite;
	/**
	 * Set of {@link CheckboxListener}s receiving events from this checkbox
	 */
	private final Set<CheckboxListener> aListeners = new HashSet<CheckboxListener>();
	/**
	 * Sprite to use when the checkbox is unchecked
	 */
	private String aUncheckedSprite;

	/**
	 * Constructor; uses default ready/not-ready sprites
	 */
	public CheckboxControl()
	{
		this("icons/icon_ready_noarrow.png", "icons/icon_ready_not_noarrow.png");
	}

	/**
	 * Constructor
	 * 
	 * @param checked
	 *            The sprite to use when the checkbox is checked
	 * @param unchecked
	 *            The sprite to use when the checkbox is unchecked
	 */
	public CheckboxControl(final String checked, final String unchecked)
	{
		super(unchecked);
		aCheckedSprite = checked;
		aUncheckedSprite = unchecked;
	}

	/**
	 * Add a {@link CheckboxListener} to the set of {@link CheckboxListener}s receiving events from this checkbox
	 * 
	 * @param listener
	 *            The {@link CheckboxListener} to add
	 */
	public void addListener(final CheckboxListener listener)
	{
		aListeners.add(listener);
	}

	/**
	 * @return True if this checkbox is checked, false otherwise
	 */
	public boolean isChecked()
	{
		return aChecked;
	}

	@Override
	public void onClick(final UIControl control)
	{
		if (!isEnabled()) {
			return;
		}
		if (aCheckable) {
			setChecked(!aChecked);
		}
		onDefocus(this); // Can't stay focused on a checkbox
	}

	@Override
	public void onDefocus(final UIControl control)
	{
		setFocusedNode(null);
	}

	@Override
	public boolean onKeyPress(final UIControl control, final KeyboardKey key)
	{
		// Do nothing
		return false;
	}

	@Override
	public boolean onKeyRelease(final UIControl control, final KeyboardKey key)
	{
		// Do nothing
		return false;
	}

	/**
	 * Set whether this checkbox can or cannot be checked by the user
	 * 
	 * @param checkable
	 *            Whether this checkbox is checkable or not
	 */
	public void setCheckable(final boolean checkable)
	{
		aCheckable = checkable;
	}

	/**
	 * Set whether this checkbox is checked or not
	 * 
	 * @param checked
	 *            Whether this checkbox is checked or not
	 */
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

	/**
	 * Alter the sprites used for the checked/unchecked states
	 * 
	 * @param checked
	 *            The sprite to use when the checkbox is checked
	 * @param unchecked
	 *            The sprite to use when the checkbox is unchecked
	 */
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

package com.evervoid.client.ui;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.jme3.math.ColorRGBA;

/**
 * A Button control.
 */
public class ButtonControl extends BorderedControl implements UIInputListener
{
	/**
	 * Default fonts for button text
	 */
	public static final String sButtonFont = "bitvoid";
	/**
	 * Default size for button text
	 */
	public static final int sButtonFontSize = 20;
	/**
	 * Default color for button text
	 */
	private static final ColorRGBA sButtonTextColor = new ColorRGBA(0.9f, 0.9f, 0.9f, 1f);
	/**
	 * Set of {@link ButtonListener}s receiving events
	 */
	private final Set<ButtonListener> aButtonObservers = new HashSet<ButtonListener>();
	/**
	 * Alpha animation used for hover
	 */
	private final AnimatedAlpha aHoverAnimation = getNewAlphaAnimation();

	/**
	 * Constructor
	 * 
	 * @param label
	 *            The text label on the button
	 */
	public ButtonControl(final String label)
	{
		super("ui/button_left.png", new CenteredBackgroundedControl(new StaticTextControl(label, sButtonTextColor, sButtonFont,
				sButtonFontSize), "ui/button_middle.png"), "ui/button_right.png");
		setHoverSelectable(true);
	}

	/**
	 * Add a {@link ButtonListener} to this button's set of {@link ButtonListener}s
	 * 
	 * @param listener
	 *            The {@link ButtonListener} to add
	 */
	public void addButtonListener(final ButtonListener listener)
	{
		aButtonObservers.add(listener);
	}

	@Override
	public void onClick(final UIControl control)
	{
		if (!isEnabled()) {
			return;
		}
		for (final ButtonListener listener : aButtonObservers) {
			listener.buttonClicked(this);
		}
		// Do not focus
		setFocusedNode(null);
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

	@Override
	public UIControl setSelected(final boolean selected)
	{
		aHoverAnimation.setTargetAlpha(selected ? 1 : 0.75).start();
		return this;
	}
}

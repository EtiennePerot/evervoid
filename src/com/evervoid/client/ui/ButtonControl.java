package com.evervoid.client.ui;

import java.util.HashSet;
import java.util.Set;

import com.jme3.math.ColorRGBA;

/**
 * A Button control.
 */
public class ButtonControl extends BorderedControl implements UIInputListener
{
	/**
	 * Default fonts for button text
	 */
	public static final String sButtonFont = "redensek";
	/**
	 * Default size for button text
	 */
	public static final int sButtonFontSize = 22;
	/**
	 * Default color for button text
	 */
	private static final ColorRGBA sButtonTextColor = new ColorRGBA(0.9f, 0.9f, 0.9f, 1f);
	/**
	 * Set of {@link ButtonListener}s receiving events
	 */
	private final Set<ButtonListener> aButtonObservers = new HashSet<ButtonListener>();

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
	public void onClick()
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
	public void onDefocus()
	{
		setFocusedNode(null);
	}
}

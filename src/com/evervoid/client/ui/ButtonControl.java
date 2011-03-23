package com.evervoid.client.ui;

import java.util.HashSet;
import java.util.Set;

import com.jme3.math.ColorRGBA;

/**
 * A Button control.
 */
public class ButtonControl extends BorderedControl implements UIInputListener
{
	public static String sButtonFont = "redensek";
	public static int sButtonFontSize = 22;
	private static ColorRGBA sButtonTextColor = new ColorRGBA(0.9f, 0.9f, 0.9f, 1f);
	private final Set<ButtonListener> aButtonObservers = new HashSet<ButtonListener>();

	public ButtonControl(final String label)
	{
		super("ui/button_left.png", new CenteredBackgroundedControl(new StaticTextControl(label, sButtonTextColor, sButtonFont,
				sButtonFontSize), "ui/button_middle.png"), "ui/button_right.png");
	}

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

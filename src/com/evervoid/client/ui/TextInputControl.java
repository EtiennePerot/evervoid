package com.evervoid.client.ui;

import com.evervoid.client.KeyboardKey;

/**
 * A text box that the user can type in.
 */
public class TextInputControl extends BorderedControl implements KeyboardFocusable
{
	private int aMaxLength;

	public TextInputControl(final int maxLength)
	{
		this("", maxLength);
	}

	public TextInputControl(final String text, final int maxLength)
	{
		super("ui/textbox_left.png", new CenteredBackgroundedControl(new SpacerControl(120, 10), "ui/textbox_middle.png"),
				"ui/textbox_right.png");
	}

	@Override
	public void defocus()
	{
		System.out.println("Defocused");
	}

	@Override
	public void focus()
	{
		System.out.println("Focused");
	}

	@Override
	public void onKeyPress(final KeyboardKey key)
	{
		System.out.println("Key press: " + key);
	}

	@Override
	public void onKeyRelease(final KeyboardKey key)
	{
		System.out.println("Key release: " + key);
	}
}

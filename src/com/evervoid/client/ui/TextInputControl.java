package com.evervoid.client.ui;

/**
 * A text box that the user can type in.
 */
public class TextInputControl extends BorderedControl
{
	public TextInputControl()
	{
		super("ui/textbox_left.png", new CenteredBackgroundedControl(new SpacerControl(120, 10), "ui/textbox_middle.png"),
				"ui/textbox_right.png");
	}
}

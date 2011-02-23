package com.evervoid.client.ui;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.jme3.math.ColorRGBA;

/**
 * A text box that the user can type in.
 */
public class TextInputControl extends BorderedControl implements UIFocusable, EVFrameObserver
{
	private static float sCursorBlinkRate = 0.4f;
	private static ColorRGBA sInputTextColor = new ColorRGBA(0.9f, 0.9f, 0.9f, 1f);
	boolean aCursorVisible = true;
	private float aLastCursorChange = 0f;
	private final int aMaxLength;
	private boolean aShiftPressed = false;
	private String aText;
	private final StaticTextControl aTextBox;

	public TextInputControl(final int maxLength)
	{
		this("", maxLength);
	}

	public TextInputControl(final String text, final int maxLength)
	{
		super("ui/textbox_left.png", new BackgroundedUIControl(BoxDirection.HORIZONTAL, "ui/textbox_middle.png"),
				"ui/textbox_right.png");
		aMaxLength = maxLength;
		aText = text;
		aTextBox = new StaticTextControl(text, sInputTextColor, ButtonControl.sButtonFont, ButtonControl.sButtonFontSize);
		addUI(new VerticalCenteredControl(aTextBox), 1);
	}

	@Override
	public void defocus()
	{
		aCursorVisible = false;
		updateText();
		EVFrameManager.deregister(this);
	}

	@Override
	public void focus()
	{
		aCursorVisible = true;
		updateText();
		EVFrameManager.register(this);
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		aLastCursorChange += f.aTpf;
		if (aLastCursorChange >= sCursorBlinkRate) {
			aLastCursorChange = 0;
			aCursorVisible = !aCursorVisible;
			updateText();
		}
	}

	public String getText()
	{
		return aText;
	}

	@Override
	public void onKeyPress(final KeyboardKey key)
	{
		if (key.isShift()) {
			aShiftPressed = true;
		}
		else if (key.equals(KeyboardKey.BACKSPACE)) {
			if (!aText.isEmpty()) {
				aText = aText.substring(0, aText.length() - 1);
				updateText();
			}
		}
		else {
			if (aText.length() < aMaxLength) {
				aText += key.getCharacter(aShiftPressed);
				updateText();
			}
		}
	}

	@Override
	public void onKeyRelease(final KeyboardKey key)
	{
		if (key.isShift()) {
			aShiftPressed = false;
		}
	}

	public void setText(final String text)
	{
		aText = text;
		updateText();
	}

	private void updateText()
	{
		if (aCursorVisible) {
			aTextBox.setText(aText + "|");
		}
		else {
			aTextBox.setText(aText);
		}
	}
}

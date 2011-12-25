package com.evervoid.client.ui;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.client.EVInputManager;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.EverVoidClient.NodeType;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.geometry.FrameTimer;
import com.evervoid.client.views.Bounds;
import com.jme3.font.LineWrapMode;
import com.jme3.math.ColorRGBA;

/**
 * A text box that the user can type in.
 */
public class TextInputControl extends BorderedControl implements UIInputListener
{
	/**
	 * Blinking rate of the user cursor, in seconds
	 */
	private static final float sCursorBlinkRate = 0.4f;
	/**
	 * The color of the text inside the text box
	 */
	private static ColorRGBA sInputTextColor = new ColorRGBA(0.9f, 0.9f, 0.9f, 1f);
	/**
	 * The {@link FrameTimer} used to make the cursor blink.
	 */
	private final FrameTimer aCursorTimer;
	/**
	 * Whether the cursor is currently visible or not
	 */
	private boolean aCursorVisible = true;
	/**
	 * The maximum length of the text the user is allowed to type
	 */
	private final int aMaxLength;
	/**
	 * Set of {@link TextInputListener}s that will receive events from this {@link TextInputControl}
	 */
	private final Set<TextInputListener> aObservers = new HashSet<TextInputListener>();
	/**
	 * Total text contained in this {@link TextInputControl}
	 */
	private String aText;
	/**
	 * The {@link StaticTextControl} used to draw the text inside the text box
	 */
	private final StaticTextControl aTextBox;
	/**
	 * Text currently visible on screen (a substring of aText)
	 */
	private String aVisibleText;
	/**
	 * The (invisible) {@link StaticTextControl} used to test for overflow
	 */
	private final StaticTextControl voidBox;

	/**
	 * Constructor
	 * 
	 * @param maxLength
	 *            The maximum number of characters that the user is allowed to type
	 */
	public TextInputControl(final int maxLength)
	{
		this("", maxLength);
	}

	/**
	 * Constructor
	 * 
	 * @param text
	 *            The initial text in the text box
	 * @param maxLength
	 *            The maximum number of characters that the user is allowed to type
	 */
	public TextInputControl(final String text, final int maxLength)
	{
		super("ui/textbox_left.png", new BackgroundedUIControl(BoxDirection.HORIZONTAL, "ui/textbox_middle.png"),
				"ui/textbox_right.png");
		aMaxLength = maxLength;
		aText = text;
		aTextBox = new StaticTextControl(text, sInputTextColor, ButtonControl.sButtonFont, ButtonControl.sButtonFontSize);
		addUI(new VerticalCenteredControl(aTextBox), 1);
		voidBox = new StaticTextControl(text, sInputTextColor, ButtonControl.sButtonFont, ButtonControl.sButtonFontSize);
		voidBox.setLineWrapMode(LineWrapMode.Character);
		EverVoidClient.addRootNode(NodeType.TWODIMENSION, voidBox);
		voidBox.getNewTransform().translate(EverVoidClient.getWindowDimension().getWidthFloat(), 0f);
		aCursorTimer = new FrameTimer(new Runnable()
		{
			@Override
			public void run()
			{
				aCursorVisible = !aCursorVisible;
				updateText();
			}
		}, sCursorBlinkRate);
	}

	/**
	 * Add a {@link TextInputListener} to the set of {@link TextInputListener}s of this {@link TextInputControl}
	 * 
	 * @param listener
	 *            The {@link TextInputListener} to add
	 */
	public void addTextInputListener(final TextInputListener listener)
	{
		aObservers.add(listener);
	}

	/**
	 * Forcibly remove focus from this {@link TextInputControl}.
	 */
	public void defocus()
	{
		aCursorTimer.stop();
		aCursorVisible = false;
		updateText();
		setFocusedNode(null);
		for (final TextInputListener listener : aObservers) {
			listener.onTextInputDefocus(this);
		}
	}

	/**
	 * Force the focus to be on this {@link TextInputControl}.
	 */
	public void focus()
	{
		if (!isEnabled()) {
			return;
		}
		aCursorVisible = true;
		updateText();
		aCursorTimer.start();
		setFocusedNode(this);
		for (final TextInputListener listener : aObservers) {
			listener.onTextInputFocus(this);
		}
	}

	@Override
	public Set<EverNode> getEffectiveChildren()
	{
		final Set<EverNode> normalChildren = super.getEffectiveChildren();
		final Set<EverNode> withExtra = new HashSet<EverNode>(normalChildren.size() + 1);
		withExtra.addAll(normalChildren);
		if (voidBox != null) {
			// since voidBox is not a direct children, we also need to consider it.
			withExtra.add(voidBox);
		}
		return withExtra;
	}

	/**
	 * @return The text in this {@link TextInputControl}.
	 */
	public String getText()
	{
		return aText;
	}

	/**
	 * @return The portion of the text that is or should currently be visible in the {@link TextInputControl}
	 */
	private String getVisibleText()
	{
		final int textLength = aText.length();
		String visibleText = "";
		int invisibleLength = 0;
		voidBox.setBounds(new Bounds(0, 0, aTextBox.getWidth(), aTextBox.getHeight()));
		voidBox.setText(aText);
		while (voidBox.getLines() > 1 && invisibleLength < textLength) {
			visibleText = aText.substring(invisibleLength);
			voidBox.setText(visibleText);
			invisibleLength++;
		}
		if (invisibleLength > 0 && invisibleLength + 1 < textLength) {
			// Leave some space for the flashing cursor.
			return aText.substring(invisibleLength + 1);
		}
		// The original string is entirely visible.
		return aText;
	}

	@Override
	public void onClick(final UIControl control)
	{
		focus();
	}

	@Override
	public void onDefocus(final UIControl control)
	{
		defocus();
	}

	@Override
	public boolean onKeyPress(final UIControl control, final KeyboardKey key)
	{
		if (!isEnabled()) {
			return false;
		}
		if (key.equals(KeyboardKey.BACKSPACE)) {
			if (!aText.isEmpty()) {
				aText = aText.substring(0, aText.length() - 1);
				updateText();
			}
		}
		else {
			if (aText.length() < aMaxLength) {
				aText += key.getCharacter(EVInputManager.shiftPressed());
				updateText();
			}
		}
		for (final TextInputListener listener : aObservers) {
			listener.onTextInputKey(this, key);
		}
		return !key.getCharacter(false).isEmpty() || key.equals(KeyboardKey.BACKSPACE) || key.equals(KeyboardKey.ENTER);
	}

	@Override
	public boolean onKeyRelease(final UIControl control, final KeyboardKey key)
	{
		// Do nothing, but mark the event as handled
		return true;
	}

	/**
	 * Change the text being displayed in this {@link TextInputControl}
	 * 
	 * @param text
	 *            The new text
	 */
	public void setText(final String text)
	{
		aText = text;
		updateText();
	}

	/**
	 * Called whenever there is a need to refresh the visual contents of the {@link TextInputControl} (keyboard press, cursor
	 * blink)
	 */
	private void updateText()
	{
		aVisibleText = getVisibleText();
		if (aCursorVisible) {
			aTextBox.setText(aVisibleText + "|");
		}
		else {
			aTextBox.setText(aVisibleText);
		}
	}
}

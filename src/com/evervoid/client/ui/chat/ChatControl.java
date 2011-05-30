package com.evervoid.client.ui.chat;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.DarkBoxControl;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.TextInputControl;
import com.evervoid.client.ui.TextInputListener;
import com.evervoid.client.ui.UIControl;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.ColorRGBA;

/**
 * A fancy chat control that the user can type into and receive replies.
 */
public class ChatControl extends PanelControl implements ButtonListener, TextInputListener
{
	/**
	 * Maximum allowed length for messages
	 */
	private static final int sMaxMessageLength = 128;
	/**
	 * Reference to the {@link ScrollingTextArea} holding the chat log
	 */
	private final ScrollingTextArea aChatLog;
	/**
	 * Reference to the "Send" {@link ButtonControl}
	 */
	private final ButtonControl aSendButton;
	/**
	 * Reference to the {@link TextInputControl} in which the user may type his message
	 */
	private final TextInputControl aTextEntry;

	/**
	 * Constructor
	 * 
	 * @param header
	 *            The title displayed as panel title
	 * @param timetamps
	 *            Whether to show timestamps in the chat log or not
	 */
	public ChatControl(final String header, final boolean timetamps)
	{
		super(header);
		aChatLog = new ScrollingTextArea(timetamps);
		addUI(new DarkBoxControl(aChatLog), 1);
		addSpacer(1, 8);
		aTextEntry = new TextInputControl(sMaxMessageLength);
		aTextEntry.addTextInputListener(this);
		aSendButton = new ButtonControl("Send");
		aSendButton.addButtonListener(this);
		final UIControl messageLine = new UIControl(BoxDirection.HORIZONTAL);
		messageLine.addUI(aTextEntry, 1);
		messageLine.addSpacer(8, 1);
		messageLine.addUI(aSendButton);
		addUI(messageLine);
		setDesiredDimension(new Dimension(400, 300));
	}

	/**
	 * Add a message to the chat log
	 * 
	 * @param player
	 *            The username of the player who sent the message
	 * @param color
	 *            The color of the username
	 * @param message
	 *            The message that was said
	 */
	public void addMessage(final String player, final ColorRGBA color, final String message)
	{
		aChatLog.addMessage(player, color, message);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		sendMessage();
	}

	/**
	 * When the panel is defocused, defocus the text box as well
	 */
	public void defocus()
	{
		aTextEntry.onDefocus();
	}

	/**
	 * When the panel is focused, automatically focus the text box
	 */
	public void focus()
	{
		aTextEntry.onClick();
	}

	@Override
	public void onTextInputDefocus(final TextInputControl control)
	{
		// Do nothing
	}

	@Override
	public void onTextInputFocus(final TextInputControl control)
	{
		// Do nothing
	}

	/**
	 * Check if the player pressed the "Enter" key; if (s)he did, send the message.
	 */
	@Override
	public void onTextInputKey(final TextInputControl control, final KeyboardKey key)
	{
		if (key.equals(KeyboardKey.ENTER)) {
			sendMessage();
		}
	}

	/**
	 * Send the message currently typed in the text box. This is called either when the player presses the Enter key, or presses
	 * the "Send" button.
	 */
	void sendMessage()
	{
		if (!aTextEntry.getText().isEmpty()) {
			EVClientEngine.sendChatMessage(aTextEntry.getText());
		}
		aTextEntry.setText(""); // Clear textbox
	}
}

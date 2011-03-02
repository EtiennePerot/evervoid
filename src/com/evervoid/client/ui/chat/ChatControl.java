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

public class ChatControl extends PanelControl implements ButtonListener, TextInputListener
{
	private static final int sMaxMessageLength = 128;
	private final ScrollingTextArea aChatLog;
	private final ButtonControl aSendButton;
	private final TextInputControl aTextEntry;

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
		setDesiredDimension(new Dimension(400, 384));
	}

	@Override
	public void buttonClicked(final ButtonControl button)
	{
		sendMessage();
	}

	public void defocus()
	{
		aTextEntry.onDefocus();
	}

	public void focus()
	{
		aTextEntry.onClick();
	}

	public void messageReceived(final String player, final ColorRGBA playerColor, final String message)
	{
		aChatLog.addMessage(player, playerColor, message);
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

	@Override
	public void onTextInputKey(final TextInputControl control, final KeyboardKey key)
	{
		if (key.equals(KeyboardKey.ENTER)) {
			sendMessage();
		}
	}

	void sendMessage()
	{
		if (!aTextEntry.getText().isEmpty()) {
			EVClientEngine.sendChatMessage(aTextEntry.getText());
		}
		aTextEntry.setText(""); // Clear textbox
	}
}

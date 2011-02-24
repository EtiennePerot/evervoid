package com.evervoid.client.ui.chat;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.TextInputControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.state.geometry.Dimension;
import com.jme3.math.ColorRGBA;

public class ChatControl extends PanelControl implements ButtonListener
{
	private final ScrollingTextArea aChatLog;
	private final ButtonControl aSendButton;
	private final TextInputControl aTextEntry;

	public ChatControl()
	{
		super("Chat");
		aChatLog = new ScrollingTextArea();
		addUI(aChatLog, 1);
		addSpacer(1, 8);
		aTextEntry = new ChatTextInputControl(this);
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

	public void messageReceived(final String player, final ColorRGBA playerColor, final String message)
	{
		aChatLog.addMessage(player, playerColor, message);
	}

	void sendMessage()
	{
		if (!aTextEntry.getText().isEmpty()) {
			EVClientEngine.sendChatMessage(aTextEntry.getText());
		}
		aTextEntry.setText(""); // Clear textbox
	}
}

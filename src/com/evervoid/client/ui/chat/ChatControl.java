package com.evervoid.client.ui.chat;

import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.TextInputControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.state.geometry.Dimension;

public class ChatControl extends PanelControl implements ButtonListener
{
	private final UIControl aChatLog;
	private final ButtonControl aSendButton;
	private final TextInputControl aTextEntry;

	public ChatControl()
	{
		super("Chat");
		aChatLog = new UIControl();
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
		setMinimumDimension(new Dimension(256, 256));
	}

	@Override
	public void buttonClicked(final ButtonControl button)
	{
		sendMessage();
	}

	void sendMessage()
	{
		System.out.println("Sending message: " + aTextEntry.getText());
		aTextEntry.setText("");
	}
}

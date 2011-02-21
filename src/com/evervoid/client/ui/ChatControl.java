package com.evervoid.client.ui;

import com.evervoid.state.geometry.Dimension;

public class ChatControl extends PanelControl
{
	private final UIControl aChatLog;
	private final TextInputControl aTextEntry;

	public ChatControl()
	{
		super("Chat");
		aChatLog = new UIControl();
		addUI(aChatLog, 1);
		addSpacer(1, 8);
		aTextEntry = new TextInputControl(20);
		addUI(aTextEntry);
		setMinimumDimension(new Dimension(256, 256));
	}
}

package com.evervoid.client.views.home;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.ui.BoxControl;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.HorizontalCenteredControl;
import com.evervoid.client.ui.StaticTextControl;
import com.jme3.math.ColorRGBA;

public class MainMenuPanel extends BoxControl implements ButtonListener
{
	private static final ColorRGBA sTitleColor = new ColorRGBA(0.9f, 0.9f, 0.9f, 1f);
	private final ButtonControl aHostGameButton;
	private final ButtonControl aJoinGameButton;

	public MainMenuPanel()
	{
		super(BoxDirection.VERTICAL);
		addUI(new HorizontalCenteredControl(new StaticTextControl("everVoid", sTitleColor, "squarehead", 32)));
		addSpacer(1, 16);
		aHostGameButton = new ButtonControl("Host game");
		aHostGameButton.addButtonListener(this);
		addUI(aHostGameButton);
		addSpacer(1, 8);
		aJoinGameButton = new ButtonControl("Join game");
		aJoinGameButton.addButtonListener(this);
		addUI(aJoinGameButton);
	}

	@Override
	public void buttonClicked(final ButtonControl button)
	{
		if (button.equals(aHostGameButton)) {
			EverVoidClient.launchLocalServer();
			EVClientEngine.connect("localhost");
		}
	}
}

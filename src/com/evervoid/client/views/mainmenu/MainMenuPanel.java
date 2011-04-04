package com.evervoid.client.views.mainmenu;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.sound.EVSoundEngine;
import com.evervoid.client.sound.Sfx;
import com.evervoid.client.ui.BoxControl;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.UIControl;
import com.jme3.math.ColorRGBA;

public class MainMenuPanel extends BoxControl implements ButtonListener
{
	private static int sButtonSpacing = 16;
	private static final ColorRGBA sTitleColor = new ColorRGBA(0.9f, 0.9f, 0.9f, 1f);
	private final ButtonControl aCreditsButton;
	private final ButtonControl aHostGameButton;
	private final ButtonControl aJoinGameButton;
	private final ButtonControl aPreferencesButton;
	private final ButtonControl aQuitGameButton;

	public MainMenuPanel()
	{
		super(BoxDirection.VERTICAL);
		addString("everVoid", sTitleColor, "squarehead", 32, BoxDirection.HORIZONTAL);
		addSpacer(1, sButtonSpacing * 2);
		aHostGameButton = new ButtonControl("Host game");
		aHostGameButton.addButtonListener(this);
		addUI(aHostGameButton);
		addSpacer(1, sButtonSpacing);
		aJoinGameButton = new ButtonControl("Join game");
		aJoinGameButton.addButtonListener(this);
		addUI(aJoinGameButton);
		addSpacer(1, sButtonSpacing * 2);
		aPreferencesButton = new ButtonControl("Preferences");
		aPreferencesButton.addButtonListener(this);
		addUI(aPreferencesButton);
		addSpacer(1, sButtonSpacing);
		aCreditsButton = new ButtonControl("Credits");
		aCreditsButton.addButtonListener(this);
		addUI(aCreditsButton);
		addSpacer(1, sButtonSpacing * 2);
		aQuitGameButton = new ButtonControl("Quit game");
		aQuitGameButton.addButtonListener(this);
		addUI(aQuitGameButton);
		setTooltip("ohai");
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		if (button.equals(aHostGameButton)) {
			EVSoundEngine.playEffect(Sfx.DOUBLE_BEEP);
			EVViewManager.switchTo(ViewType.LOADING);
			(new Thread()
			{
				@Override
				public void run()
				{
					EVClientEngine.startLocalServer();
					EVClientEngine.connect("localhost");
				}
			}).start();
		}
		else if (button.equals(aJoinGameButton)) {
			EVSoundEngine.playEffect(Sfx.BEEP);
			EVViewManager.switchTo(ViewType.SERVERLIST);
		}
		else if (button.equals(aPreferencesButton)) {
			EVSoundEngine.playEffect(Sfx.BEEP);
			EVViewManager.switchTo(ViewType.PREFERENCES);
		}
		else if (button.equals(aCreditsButton)) {
			EVSoundEngine.playEffect(Sfx.BEEP);
			EVViewManager.switchTo(ViewType.CREDITS);
		}
		else if (button.equals(aQuitGameButton)) {
			EVSoundEngine.playEffect(Sfx.BEEP_BACK);
			EverVoidClient.quit();
		}
	}
}

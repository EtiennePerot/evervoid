package com.evervoid.client.views.lobby;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.EVClientSaver;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.network.lobby.LobbyPlayer;
import com.evervoid.network.lobby.LobbyState;

public class LobbyOptionsPanel extends PanelControl implements ButtonListener
{
	private final ColorSelectionControl aColorSelector;
	private final ButtonControl aLoadGameButton;
	private final RaceSelectionControl aRaceSelector;
	private final ButtonControl aStartGameButton;
	private final LobbyView aView;

	public LobbyOptionsPanel(final LobbyView view, final LobbyState state)
	{
		super("Settings");
		aView = view;
		aRaceSelector = new RaceSelectionControl(aView, state);
		addSpacer(1, 16); // TODO: Put a pretty picture here perhaps
		addUI(aRaceSelector);
		addFlexSpacer(1);
		aColorSelector = new ColorSelectionControl(aView, state);
		addUI(aColorSelector);
		addFlexSpacer(1);
		aLoadGameButton = new ButtonControl("Load game");
		aLoadGameButton.addButtonListener(this);
		addUI(aLoadGameButton);
		if (EVClientSaver.getAvailableSaveFiles().isEmpty()) {
			aLoadGameButton.disable();
		}
		addSpacer(1, 16);
		aStartGameButton = new ButtonControl("Start game");
		aStartGameButton.addButtonListener(this);
		addUI(aStartGameButton);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		if (button.equals(aStartGameButton)) {
			EVClientEngine.sendStartGame();
		}
		else if (button.equals(aLoadGameButton)) {
			aView.promptLoad();
		}
	}

	void updateData(final LobbyState state, final LobbyPlayer self)
	{
		aRaceSelector.setRace(self.getRace(), false);
		aColorSelector.setColor(self.getColorName(), false);
	}
}
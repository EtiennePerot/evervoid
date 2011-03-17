package com.evervoid.client.views.lobby;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.network.lobby.LobbyPlayer;
import com.evervoid.network.lobby.LobbyState;

public class LobbyOptionsPanel extends PanelControl implements ButtonListener
{
	private final ColorSelectionControl aColorSelector;
	private final RaceSelectionControl aRaceSelector;

	public LobbyOptionsPanel(final LobbyView view, final LobbyState state)
	{
		super("Settings");
		aRaceSelector = new RaceSelectionControl(view, state);
		addSpacer(1, 16); // TODO: Put a pretty picture here perhaps
		addUI(aRaceSelector);
		addFlexSpacer(1);
		aColorSelector = new ColorSelectionControl(view, state);
		addUI(aColorSelector);
		addFlexSpacer(1);
		// TODO: Add "Load save game" button
		final ButtonControl goButton = new ButtonControl("Start game");
		goButton.addButtonListener(this);
		addUI(goButton);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		EVClientEngine.sendStartGame();
	}

	void updateData(final LobbyState state, final LobbyPlayer self)
	{
		aRaceSelector.setRace(self.getRace(), false);
		aColorSelector.setColor(self.getColorName(), false);
	}
}

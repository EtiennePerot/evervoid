package com.evervoid.client.views.lobby;

import com.evervoid.client.ui.PanelControl;
import com.evervoid.network.lobby.LobbyPlayer;
import com.evervoid.network.lobby.LobbyState;

public class LobbyOptionsPanel extends PanelControl
{
	private final RaceSelectionControl aRaceSelector;

	public LobbyOptionsPanel(final LobbyView view, final LobbyState state)
	{
		super("Settings");
		aRaceSelector = new RaceSelectionControl(view, state);
		addSpacer(1, 16); // TODO: Put a pretty picture here perhaps
		addUI(aRaceSelector);
		// Add more
		addFlexSpacer(1);
	}

	void updateData(final LobbyState state, final LobbyPlayer self)
	{
		aRaceSelector.raceChanged(self.getRace(), false);
	}
}

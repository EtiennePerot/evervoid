package com.evervoid.client.views.lobby;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.network.lobby.LobbyState;
import com.evervoid.state.data.GameData;

public class RaceSelectionControl extends UIControl
{
	private static final int sRaceSpacerSize = 12;
	private final LobbyView aLobbyView;
	private final Map<String, SelectableRaceButton> aRaceButtons = new HashMap<String, SelectableRaceButton>();

	public RaceSelectionControl(final LobbyView view, final LobbyState state)
	{
		super(BoxDirection.VERTICAL);
		aLobbyView = view;
		addString("Race:", PanelControl.sPanelTitleColor);
		addSpacer(1, sRaceSpacerSize);
		final GameData data = state.getGameData();
		for (final String race : data.getRaceTypes()) {
			final SelectableRaceButton button = new SelectableRaceButton(data.getRaceData(race), this);
			aRaceButtons.put(race, button);
			addUI(button);
			addSpacer(1, sRaceSpacerSize);
		}
	}

	void setRace(final String race, final boolean sendUpdate)
	{
		for (final String r : aRaceButtons.keySet()) {
			aRaceButtons.get(r).setActive(race.equals(r));
		}
		if (sendUpdate) {
			aLobbyView.setPlayerRace(race);
		}
	}
}

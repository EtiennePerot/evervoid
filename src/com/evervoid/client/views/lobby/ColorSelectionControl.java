package com.evervoid.client.views.lobby;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.network.lobby.LobbyState;
import com.evervoid.state.data.GameData;

public class ColorSelectionControl extends UIControl
{
	private static final int sColorSpacerSize = 12;
	private static final int sColorsPerRow = 5;
	private final Map<String, SelectableColorButton> aColorButtons = new HashMap<String, SelectableColorButton>();
	private final LobbyView aLobbyView;

	public ColorSelectionControl(final LobbyView view, final LobbyState state)
	{
		super(BoxDirection.VERTICAL);
		aLobbyView = view;
		addString("Color:", PanelControl.sPanelTitleColor, "bitvoid", 20);
		final GameData data = state.getGameData();
		int rowIndex = 0;
		UIControl row = null;
		for (final String color : data.getPlayerColors()) {
			if (rowIndex == 0) {
				addSpacer(1, sColorSpacerSize);
				row = new UIControl(BoxDirection.HORIZONTAL);
				addUI(row);
			}
			final SelectableColorButton button = new SelectableColorButton(color, this);
			aColorButtons.put(color, button);
			row.addUI(button);
			rowIndex++;
			if (rowIndex == sColorsPerRow) {
				rowIndex = 0;
			}
			else {
				row.addSpacer(sColorSpacerSize, 1);
				row.addFlexSpacer(1);
			}
		}
		addSpacer(1, sColorSpacerSize);
	}

	void setColor(final String color, final boolean sendUpdate)
	{
		if (sendUpdate) {
			aLobbyView.setPlayerColor(color);
		}
		else {
			for (final String c : aColorButtons.keySet()) {
				aColorButtons.get(c).setActive(color.equals(c));
			}
		}
	}
}

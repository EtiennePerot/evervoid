package com.evervoid.client.views.lobby;

import com.evervoid.client.ui.BoxControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.server.LobbyPlayer;
import com.jme3.math.ColorRGBA;

public class LobbyPlayerEntry extends BoxControl
{
	private static ColorRGBA sPlayerNameColor = new ColorRGBA(0.75f, 0.75f, 0.75f, 1f);
	private final StaticTextControl aPlayerName;

	LobbyPlayerEntry()
	{
		super(BoxDirection.VERTICAL);
		aPlayerName = new StaticTextControl("", sPlayerNameColor);
		addUI(aPlayerName);
	}

	void removeEntry()
	{
		// TODO: Fancy alpha animation
		delete();
	}

	void updatePlayer(final LobbyPlayer player)
	{
		aPlayerName.setText(player.getNickname());
	}
}

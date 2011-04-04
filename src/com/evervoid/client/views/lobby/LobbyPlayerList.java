package com.evervoid.client.views.lobby;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.sound.EVSoundEngine;
import com.evervoid.client.sound.Sfx;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.network.lobby.LobbyPlayer;
import com.evervoid.network.lobby.LobbyState;

public class LobbyPlayerList extends PanelControl implements ButtonListener
{
	private final LobbyView aLobbyView;
	List<LobbyPlayerEntry> aPlayerEntries = new ArrayList<LobbyPlayerEntry>();
	private final UIControl aPlayerListControl;

	LobbyPlayerList(final LobbyView view)
	{
		super("everVoid Lobby"); // Default name; overridden later with lobby data
		aLobbyView = view;
		final ButtonControl backToMain = new ButtonControl("Leave lobby");
		backToMain.addButtonListener(this);
		getTitleBox().addUI(backToMain);
		aPlayerListControl = new UIControl(BoxDirection.VERTICAL);
		addUI(aPlayerListControl);
		addUI(new UIControl(), 1); // Spring-y spacer at the bottom to make sure the player list snaps to the top
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		EVSoundEngine.playEffect(Sfx.BEEP_BACK);
		aLobbyView.leaveLobby();
	}

	/**
	 * Gets the Player Entry control at the given index. If it does not exist, creates it and adds it to the UI.
	 * 
	 * @param index
	 *            The index of the entry
	 * @return Reference to the entry
	 */
	LobbyPlayerEntry getPlayerEntry(final int index)
	{
		if (index >= aPlayerEntries.size()) {
			final LobbyPlayerEntry entry = new LobbyPlayerEntry(aLobbyView);
			aPlayerEntries.add(entry);
			aPlayerListControl.addUI(entry);
			return entry;
		}
		return aPlayerEntries.get(index);
	}

	void updateData(final LobbyState lobby, final LobbyPlayer self)
	{
		setTitle(lobby.getServerName());
		int index = 0;
		final int total = lobby.getNumOfPlayers();
		for (final LobbyPlayer player : lobby) {
			getPlayerEntry(index).updateEntry(player, player.equals(self), lobby.getGameData(), index == 0, index == total - 1);
			index++;
		}
		// Remove leftover entries if there are too many of them (player disconnected, etc)
		for (int i = aPlayerEntries.size() - 1; i >= index; i--) {
			final LobbyPlayerEntry entry = aPlayerEntries.get(i);
			entry.removeEntry();
			aPlayerEntries.remove(i);
		}
	}
}

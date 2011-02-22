package com.evervoid.client.views.lobby;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.ui.PanelControl;
import com.evervoid.server.LobbyPlayer;
import com.evervoid.server.LobbyState;

public class LobbyPlayerList extends PanelControl
{
	List<LobbyPlayerEntry> aPlayerEntries = new ArrayList<LobbyPlayerEntry>();

	LobbyPlayerList()
	{
		super("everVoid Lobby"); // Default name; overridden later with lobby data
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
			final LobbyPlayerEntry entry = new LobbyPlayerEntry();
			aPlayerEntries.add(entry);
			addUI(entry);
			return entry;
		}
		return aPlayerEntries.get(index);
	}

	void updateData(final LobbyState lobby)
	{
		setTitle(lobby.getServerName());
		int index = 0;
		for (final LobbyPlayer player : lobby) {
			getPlayerEntry(index).updatePlayer(player);
			index++;
		}
		// Remove entries if there are too much (player disconnected, etc)
		for (int i = aPlayerEntries.size() - 1; i >= index; i--) {
			final LobbyPlayerEntry entry = aPlayerEntries.get(i);
			entry.removeEntry();
			aPlayerEntries.remove(i);
		}
		System.out.println(getRootUI());
	}
}

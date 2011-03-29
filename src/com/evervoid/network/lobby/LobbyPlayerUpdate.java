package com.evervoid.network.lobby;

import com.evervoid.network.EverMessage;

public class LobbyPlayerUpdate extends EverMessage
{
	public LobbyPlayerUpdate(final LobbyPlayer player)
	{
		super(player, LobbyPlayerUpdate.class.getName());
	}
}

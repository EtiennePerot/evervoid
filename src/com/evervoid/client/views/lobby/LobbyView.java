package com.evervoid.client.views.lobby;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.interfaces.EVLobbyMessageListener;
import com.evervoid.client.ui.BoxControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverView;
import com.evervoid.json.Json;

public class LobbyView extends EverView implements EVLobbyMessageListener
{
	public LobbyView()
	{
		EVClientEngine.registerLobbyListener(this);
		final UIControl root = new BoxControl();
		root.setBounds(new Bounds(128, 64, 512, 256));
		addNode(root);
		System.out.println(root);
	}

	@Override
	public void receivedGameData(final Json gameData)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void receivedLobbyData(final Json playerData)
	{
		// Switch to lobby view when we get new player data
		EVViewManager.switchTo(ViewType.LOBBY);
	}

	@Override
	public void receivedStartGame()
	{
		// TODO Auto-generated method stub
	}
}

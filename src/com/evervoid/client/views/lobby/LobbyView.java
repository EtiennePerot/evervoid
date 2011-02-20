package com.evervoid.client.views.lobby;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.interfaces.EVLobbyMessageListener;
import com.evervoid.client.ui.UIConnector;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.EverView;
import com.evervoid.json.Json;
import com.evervoid.state.geometry.Dimension;

public class LobbyView extends EverView implements EVLobbyMessageListener
{
	public LobbyView()
	{
		EVClientEngine.registerLobbyListener(this);
		final UIControl root = new UIControl();
		root.addUI(new UIConnector("ui/menubox/left_corner_bottom.png"));
		root.addUI(new UIConnector("ui/menubox/horizontal.png"), 1);
		root.addUI(new UIConnector("ui/menubox/right_corner_bottom.png"));
		root.sizeTo(new Dimension(90, 60));
		addNode(root);
		System.out.println(root);
		System.out.println("Lobby view created");
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

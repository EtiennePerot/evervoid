package com.evervoid.client.views.lobby;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.interfaces.EVLobbyMessageListener;
import com.evervoid.client.ui.BoxControl;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.SpacerControl;
import com.evervoid.client.ui.TextInputControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverView;
import com.evervoid.json.Json;

public class LobbyView extends EverView implements EVLobbyMessageListener
{
	private final UIControl aRootUI;

	public LobbyView()
	{
		EVClientEngine.registerLobbyListener(this);
		aRootUI = new BoxControl(BoxDirection.VERTICAL);
		aRootUI.addUI(new ButtonControl("aaa test this is a test"));
		aRootUI.addUI(new SpacerControl(1, 6));
		aRootUI.addUI(new TextInputControl());
		addNode(aRootUI);
		resolutionChanged();
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

	@Override
	public void resolutionChanged()
	{
		aRootUI.setBounds(new Bounds(32, 32, EverVoidClient.getWindowDimension().width - 64, EverVoidClient
				.getWindowDimension().height - 64));
		System.out.println(aRootUI);
	}
}

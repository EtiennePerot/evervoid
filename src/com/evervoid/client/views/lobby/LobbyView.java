package com.evervoid.client.views.lobby;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.interfaces.EVLobbyMessageListener;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.ui.chat.ChatControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverView;
import com.evervoid.json.Json;
import com.jme3.math.Vector2f;

public class LobbyView extends EverView implements EVLobbyMessageListener
{
	private final UIControl aChatPanel;
	private final UIControl aPlayerList;
	private final UIControl aRootUI;
	private final UIControl aSidePanel;

	public LobbyView()
	{
		EVClientEngine.registerLobbyListener(this);
		aRootUI = new UIControl(BoxDirection.HORIZONTAL);
		final UIControl leftSide = new UIControl(BoxDirection.VERTICAL);
		aPlayerList = new PanelControl("Players");
		aChatPanel = new ChatControl();
		leftSide.addUI(aPlayerList, 1);
		leftSide.addUI(aChatPanel, 0);
		aSidePanel = new PanelControl("Game info and stuff");
		aRootUI.addUI(leftSide, 1);
		aRootUI.addUI(aSidePanel, 0);
		addNode(aRootUI);
		resolutionChanged();
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		aRootUI.onKeyPress(key);
		return true;
	}

	@Override
	public boolean onKeyRelease(final KeyboardKey key, final float tpf)
	{
		aRootUI.onKeyRelease(key);
		return true;
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		aRootUI.click(position);
		return true;
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

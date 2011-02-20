package com.evervoid.client.views.lobby;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.interfaces.EVLobbyMessageListener;
import com.evervoid.client.ui.BoxControl;
import com.evervoid.client.ui.Sizer.SizerDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.json.Json;
import com.jme3.app.SimpleApplication;

public class LobbyView extends SimpleApplication implements EVLobbyMessageListener
{
	public static void main(final String[] args)
	{
		new LobbyView().start();
	}

	public LobbyView()
	{
		EVClientEngine.registerLobbyListener(this);
	}

	@Override
	public void receivedGameData(final Json gameData)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void receivedPlayerData(final Json playerData)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void receivedStartGame()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void simpleInitApp()
	{
		GraphicManager.setAssetManager(assetManager);
		guiNode.attachChild(new BoxControl(new Bounds(0, 0, 50, 50), SizerDirection.HORIZONTAL));
		guiNode.attachChild(new Sprite("ui/menubox/right_corner_bottom.png"));
		System.out.println(new BoxControl(new Bounds(0, 0, 50, 50), SizerDirection.HORIZONTAL));
	}
}

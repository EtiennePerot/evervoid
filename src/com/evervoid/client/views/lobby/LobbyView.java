package com.evervoid.client.views.lobby;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.interfaces.EVLobbyMessageListener;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.PanelControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.ui.chat.ChatControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverView;
import com.evervoid.network.lobby.LobbyPlayer;
import com.evervoid.network.lobby.LobbyState;
import com.evervoid.state.Color;
import com.jme3.math.Vector2f;

public class LobbyView extends EverView implements EVLobbyMessageListener, ButtonListener
{
	private final ChatControl aChatPanel;
	private LobbyState aLobbyInfo;
	private LobbyPlayer aMe;
	private final LobbyPlayerList aPlayerList;
	private final UIControl aRootUI;
	private final UIControl aSidePanel;

	public LobbyView(final LobbyState lobby)
	{
		aPlayerList = new LobbyPlayerList();
		aLobbyInfo = lobby;
		EVClientEngine.registerLobbyListener(this);
		aRootUI = new UIControl(BoxDirection.HORIZONTAL);
		final UIControl leftSide = new UIControl(BoxDirection.VERTICAL);
		aChatPanel = new ChatControl();
		leftSide.addUI(aPlayerList, 1);
		leftSide.addUI(aChatPanel, 0);
		aSidePanel = new PanelControl("Game info and stuff");
		aRootUI.addUI(leftSide, 1);
		aRootUI.addUI(aSidePanel, 0);
		// FIXME: Ugly UI to get a quick start game button
		final ButtonControl goButton = new ButtonControl("Start game");
		goButton.addButtonListener(this);
		aSidePanel.addUI(goButton);
		addNode(aRootUI);
		resolutionChanged();
		updateLobbyInfo();
	}

	@Override
	public void buttonClicked(final ButtonControl button)
	{
		EVClientEngine.sendStartGame();
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
	public void receivedChat(final String player, final Color playerColor, final String message)
	{
		aChatPanel.messageReceived(player, GraphicsUtils.getColorRGBA(playerColor), message);
	}

	@Override
	public void receivedLobbyData(final LobbyState state)
	{
		EVViewManager.switchTo(ViewType.LOBBY);
		aLobbyInfo = state;
		updateLobbyInfo();
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
	}

	public void updateLobbyInfo()
	{
		aPlayerList.updateData(aLobbyInfo);
		for (final LobbyPlayer player : aLobbyInfo) {
			if (player.getNickname().equals(EverVoidClient.getSettings().getNickname())) {
				aMe = player;
			}
		}
	}
}

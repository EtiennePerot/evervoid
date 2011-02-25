package com.evervoid.client.views.lobby;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.EVFrameManager;
import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.evervoid.client.interfaces.EVLobbyMessageListener;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.ui.chat.ChatControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverView;
import com.evervoid.network.lobby.LobbyPlayer;
import com.evervoid.network.lobby.LobbyState;
import com.evervoid.state.Color;
import com.jme3.math.Vector2f;

public class LobbyView extends EverView implements EVLobbyMessageListener, EVFrameObserver
{
	private final ChatControl aChatPanel;
	private LobbyState aLobbyInfo;
	private LobbyPlayer aMe;
	private final LobbyOptionsPanel aOptionsPanel;
	private final LobbyPlayerList aPlayerList;
	private final UIControl aRootUI;
	private final BlockingQueue<Runnable> aUIJobs = new LinkedBlockingQueue<Runnable>();

	public LobbyView(final LobbyState lobby)
	{
		aPlayerList = new LobbyPlayerList(this);
		aLobbyInfo = lobby;
		EVClientEngine.registerLobbyListener(this);
		EVFrameManager.register(this);
		aRootUI = new UIControl(BoxDirection.HORIZONTAL);
		final UIControl leftSide = new UIControl(BoxDirection.VERTICAL);
		aChatPanel = new ChatControl("Chat", true);
		leftSide.addUI(aPlayerList, 1);
		leftSide.addUI(aChatPanel, 0);
		aOptionsPanel = new LobbyOptionsPanel(this, lobby);
		aRootUI.addUI(leftSide, 1);
		aRootUI.addUI(aOptionsPanel, 0);
		addNode(aRootUI);
		resolutionChanged();
		updateLobbyInfo();
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		while (!aUIJobs.isEmpty()) {
			aUIJobs.poll().run();
		}
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

	private void sendPlayerData()
	{
		EVClientEngine.sendLobbyPlayer(aMe);
	}

	void setPlayerColor(final String colorname)
	{
		if (aMe != null && aMe.setColor(colorname)) {
			sendPlayerData();
		}
	}

	void setPlayerRace(final String race)
	{
		if (aMe != null && aMe.setRace(race)) {
			sendPlayerData();
		}
	}

	void setPlayerReady(final boolean ready)
	{
		if (aMe != null && aMe.setReady(ready)) {
			sendPlayerData();
		}
	}

	public void updateLobbyInfo()
	{
		aUIJobs.add(new Runnable()
		{
			@Override
			public void run()
			{
				aMe = null;
				for (final LobbyPlayer player : aLobbyInfo) {
					if (player.getNickname().equals(EverVoidClient.getSettings().getNickname())) {
						aMe = player;
					}
				}
				aPlayerList.updateData(aLobbyInfo, aMe);
				aOptionsPanel.updateData(aLobbyInfo, aMe);
			}
		});
	}
}

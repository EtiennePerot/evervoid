package com.evervoid.client.views.lobby;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.EVFrameManager;
import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.evervoid.client.interfaces.EVLobbyMessageListener;
import com.evervoid.client.ui.FilePicker;
import com.evervoid.client.ui.FilePicker.FilePickerMode;
import com.evervoid.client.ui.FilePickerListener;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.ui.chat.ChatControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;
import com.evervoid.network.lobby.LobbyPlayer;
import com.evervoid.network.lobby.LobbyState;
import com.evervoid.state.Color;
import com.jme3.math.ColorRGBA;

public class LobbyView extends EverUIView implements EVLobbyMessageListener, EVFrameObserver, FilePickerListener
{
	private static final ColorRGBA sClientMessagesColor = new ColorRGBA(0.6f, 0.95f, 0.8f, 1f);
	private final ChatControl aChatPanel;
	private final FilePicker aLoadFilePicker = new FilePicker(FilePickerMode.LOAD);
	private LobbyState aLobbyInfo;
	private LobbyPlayer aMe;
	private final LobbyOptionsPanel aOptionsPanel;
	private final LobbyPlayerList aPlayerList;
	private final BlockingQueue<Runnable> aUIJobs = new LinkedBlockingQueue<Runnable>();

	public LobbyView(final LobbyState lobby)
	{
		super(new UIControl(BoxDirection.HORIZONTAL));
		aPlayerList = new LobbyPlayerList(this);
		aLobbyInfo = lobby;
		EVClientEngine.registerLobbyListener(this);
		EVFrameManager.register(this);
		final UIControl leftSide = new UIControl(BoxDirection.VERTICAL);
		aChatPanel = new ChatControl("Chat", true);
		leftSide.addUI(aPlayerList, 1);
		leftSide.addUI(aChatPanel, 0);
		aOptionsPanel = new LobbyOptionsPanel(this, lobby);
		addUI(leftSide, 1);
		addUI(aOptionsPanel, 0);
		aLoadFilePicker.registerListener(this);
		setBounds(Bounds.getWholeScreenBounds());
		updateLobbyInfo();
	}

	public void addClientMessage(final String message)
	{
		aChatPanel.addMessage("Client", sClientMessagesColor, message);
	}

	@Override
	public void filePicked(final FilePicker picker, final FilePickerMode mode, final File file)
	{
		deleteUI();
		addClientMessage("Sending save file " + file.getName() + " to the server...");
		EVClientEngine.sendLoadGame(file, new Runnable()
		{
			@Override
			public void run()
			{
				addClientMessage("Error while sending save file: " + file.getName() + ".");
			}
		});
	}

	@Override
	public void filePickerCancelled(final FilePicker picker, final FilePickerMode mode)
	{
		deleteUI();
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		while (!aUIJobs.isEmpty()) {
			aUIJobs.poll().run();
		}
	}

	public void leaveLobby()
	{
		EVClientEngine.disconnect();
		EVViewManager.switchTo(ViewType.MAINMENU);
	}

	void promptLoad()
	{
		pushUI(aLoadFilePicker);
	}

	@Override
	public void receivedChat(final String player, final Color playerColor, final String message)
	{
		aChatPanel.messageReceived(player, GraphicsUtils.getColorRGBA(playerColor), message);
	}

	@Override
	public void receivedLobbyData(final LobbyState state)
	{
		// TODO: This should check if the Lobby GameData has changed from the previous one (may happen when loading a game). In
		// these cases, it should rebuild the entire view.
		EVViewManager.switchTo(ViewType.LOBBY);
		aLobbyInfo = state;
		updateLobbyInfo();
	}

	@Override
	public void receivedStartGame()
	{
		// Nothing
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

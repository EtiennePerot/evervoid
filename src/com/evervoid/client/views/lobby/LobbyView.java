package com.evervoid.client.views.lobby;

import java.io.File;

import com.evervoid.client.EVClientEngine;
import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.interfaces.EVLobbyMessageListener;
import com.evervoid.client.ui.ErrorMessageDialog;
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

public class LobbyView extends EverUIView implements EVLobbyMessageListener, FilePickerListener
{
	private static final ColorRGBA sClientMessagesColor = new ColorRGBA(0.6f, 0.95f, 0.8f, 1f);
	private final ChatControl aChatPanel;
	private final FilePicker aLoadFilePicker = new FilePicker(FilePickerMode.LOAD);
	private LobbyState aLobbyInfo;
	private final LobbyOptionsPanel aOptionsPanel;
	private final LobbyPlayerList aPlayerList;

	public LobbyView(final LobbyState lobby)
	{
		super(new UIControl(BoxDirection.HORIZONTAL));
		aPlayerList = new LobbyPlayerList(this);
		aLobbyInfo = lobby;
		EVClientEngine.registerLobbyListener(this);
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
		final String defaultNick = EverVoidClient.getSettings().getPlayerNickname();
		if (!defaultNick.equalsIgnoreCase(getLocalPlayerName())) {
			addClientMessage("Your nickname \"" + defaultNick
					+ "\" was already in use in the lobby. You have been renamed to \"" + getLocalPlayerName() + "\".");
		}
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
	public void filePickerCanceled(final FilePicker picker, final FilePickerMode mode)
	{
		deleteUI();
	}

	public LobbyPlayer getLocalPlayer()
	{
		return aLobbyInfo.getLocalPlayer();
	}

	public String getLocalPlayerName()
	{
		return aLobbyInfo.getLocalPlayer().getNickname();
	}

	public void leaveLobby()
	{
		EVClientEngine.disconnect();
		EVViewManager.switchTo(ViewType.MAINMENU);
		EVViewManager.deregisterView(ViewType.LOBBY, null);
	}

	void promptLoad()
	{
		pushUI(aLoadFilePicker);
	}

	public void promptName()
	{
		pushUI(new PlayerRenamingDialog(this));
	}

	@Override
	public void receivedChat(final String player, final Color playerColor, final String message)
	{
		aChatPanel.addMessage(player, GraphicsUtils.getColorRGBA(playerColor), message);
	}

	@Override
	public void receivedLobbyData(final LobbyState state)
	{
		// TODO: This should check if the Lobby GameData has changed from the previous one (may happen when loading a game).
		// In these cases, it should rebuild the entire view.
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
		EVClientEngine.sendLobbyPlayer(getLocalPlayer());
	}

	@Override
	public void serverDied()
	{
		leaveLobby();
	}

	void setPlayerColor(final String colorname)
	{
		if (getLocalPlayer() != null && getLocalPlayer().setColor(colorname)) {
			sendPlayerData();
		}
	}

	void setPlayerName(final String name)
	{
		deleteUI();
		if (getLocalPlayer() == null || name.equals(getLocalPlayerName())) {
			return;
		}
		final LobbyPlayer otherPlayer = aLobbyInfo.getPlayerByNickname(name);
		if (otherPlayer != null) {
			pushUI(new ErrorMessageDialog("The nickname \"" + name + "\" is already taken.", new Runnable()
			{
				@Override
				public void run()
				{
					deleteUI();
				}
			}));
			return;
		}
		if (getLocalPlayer().setNickname(name)) {
			sendPlayerData();
		}
	}

	void setPlayerRace(final String race)
	{
		if (getLocalPlayer() != null && getLocalPlayer().setRace(race)) {
			sendPlayerData();
		}
	}

	void setPlayerReady(final boolean ready)
	{
		if (getLocalPlayer() != null && getLocalPlayer().setReady(ready)) {
			sendPlayerData();
		}
	}

	public void updateLobbyInfo()
	{
		aPlayerList.updateData(aLobbyInfo);
		aOptionsPanel.updateData(aLobbyInfo);
	}
}

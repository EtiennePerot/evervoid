package com.evervoid.client;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.interfaces.EVGameMessageListener;
import com.evervoid.client.interfaces.EVGlobalMessageListener;
import com.evervoid.client.interfaces.EVLobbyMessageListener;
import com.evervoid.client.views.GameView;
import com.evervoid.client.views.lobby.LobbyView;
import com.evervoid.json.Json;
import com.evervoid.network.ChatMessage;
import com.evervoid.network.EverMessage;
import com.evervoid.network.EverMessageHandler;
import com.evervoid.network.EverMessageListener;
import com.evervoid.network.HandshakeMessage;
import com.evervoid.network.StartGameMessage;
import com.evervoid.network.TurnMessage;
import com.evervoid.network.lobby.LobbyState;
import com.evervoid.server.EVServerMessageObserver;
import com.evervoid.state.Color;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Turn;
import com.jme3.network.connection.Client;

public class EVClientEngine implements EverMessageListener
{
	public static final Logger sConnectionLog = Logger.getLogger(EVClientEngine.class.getName());
	private static EVClientEngine sInstance;

	public static EVClientEngine getInstance()
	{
		if (sInstance == null) {
			sInstance = new EVClientEngine("localhost");
		}
		return sInstance;
	}

	public static void registerGameListener(final EVGameMessageListener listener)
	{
		sInstance.aGameObservers.add(listener);
	}

	public static void registerGlobalListener(final EVGlobalMessageListener listener)
	{
		sInstance.aGlobalObservers.add(listener);
	}

	public static void registerLobbyListener(final EVLobbyMessageListener listener)
	{
		sInstance.aLobbyObservers.add(listener);
	}

	public static void sendMessage(final String message)
	{
		sInstance.aMessageHandler.send(new ChatMessage(message));
	}

	public static void sendStartGame()
	{
		sInstance.aMessageHandler.send(new StartGameMessage());
	}

	public static void sendTurn(final Turn turn)
	{
		sInstance.aMessageHandler.send(new TurnMessage(turn));
	}

	private Client aClient;
	private final Set<EVGameMessageListener> aGameObservers = new HashSet<EVGameMessageListener>();
	private final Set<EVGlobalMessageListener> aGlobalObservers = new HashSet<EVGlobalMessageListener>();
	private boolean aInLobby = false;
	private final Set<EVLobbyMessageListener> aLobbyObservers = new HashSet<EVLobbyMessageListener>();
	private final EverMessageHandler aMessageHandler;
	private final String aServerIP;
	private final int aTCPport;
	private final int aUDPport;

	/**
	 * Initialize a connection to a server using default ports.
	 * 
	 * @param pServerIP
	 *            Address of the server.
	 */
	protected EVClientEngine(final String pServerIP)
	{
		this(pServerIP, 51255, 51255);
	}

	/**
	 * Initialise a connection with specified TCP and UDP ports.
	 * 
	 * @param pServerIP
	 *            Address of the server.
	 * @param pTCPport
	 *            TCP port to use.
	 * @param pUDPport
	 *            UDP port to use.
	 * @param listener
	 *            Listener to notify
	 */
	public EVClientEngine(final String pServerIP, final int pTCPport, final int pUDPport)
	{
		sConnectionLog.setLevel(Level.ALL);
		sConnectionLog.info("Client connecting to " + pServerIP + " on ports " + pTCPport + "; " + pUDPport);
		aServerIP = new String(pServerIP);
		aTCPport = pTCPport;
		aUDPport = pUDPport;
		try {
			aClient = new Client(aServerIP, aTCPport, aUDPport);
		}
		catch (final IOException e) {
			sConnectionLog.severe("Could not establish connection to server. IOException caught.");
		}
		sConnectionLog.info("Client connected to " + pServerIP + ": " + aClient);
		aMessageHandler = new EverMessageHandler(aClient);
		aMessageHandler.addMessageListener(this);
		aClient.start();
		try {
			Thread.sleep(500);
		}
		catch (final InterruptedException e1) {
			e1.printStackTrace();
		}
		sConnectionLog.info("Client started.");
		aMessageHandler.send(new HandshakeMessage(EverVoidClient.getSettings().getNickname()));
		sConnectionLog.info("Client message sent to server.");
	}

	public void deregisterObserver(final EVServerMessageObserver observer)
	{
		aGlobalObservers.remove(observer);
	}

	@Override
	public void messageReceived(final EverMessage message)
	{
		sConnectionLog.info("Client received: " + message + " | " + message.getJson());
		final String messageType = message.getType();
		final Json messageContents = message.getJson();
		if (messageType.equals("gamestate")) {
			for (final EVGlobalMessageListener observer : aGlobalObservers) {
				observer.receivedGameState(new EVGameState(messageContents));
			}
		}
		else if (messageType.equals("lobbydata")) {
			final LobbyState lobbyState = new LobbyState(messageContents);
			if (!aInLobby) {
				aInLobby = true;
				EVViewManager.schedule(new Runnable()
				{
					@Override
					public void run()
					{
						EVViewManager.registerView(ViewType.LOBBY, new LobbyView(lobbyState));
						EVViewManager.switchTo(ViewType.LOBBY);
					}
				});
			}
			for (final EVLobbyMessageListener observer : aLobbyObservers) {
				observer.receivedLobbyData(lobbyState);
			}
		}
		else if (messageType.equals("chat")) {
			for (final EVGlobalMessageListener observer : aGlobalObservers) {
				observer.receivedChat(messageContents.getStringAttribute("player"),
						new Color(messageContents.getAttribute("color")), messageContents.getStringAttribute("message"));
			}
		}
		else if (messageType.equals("turn")) {
			for (final EVGameMessageListener observer : aGameObservers) {
				observer.receivedTurn(new Turn(messageContents, GameView.getGameState()));
			}
		}
	}
}

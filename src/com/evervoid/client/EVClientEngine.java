package com.evervoid.client;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evervoid.client.interfaces.EVGameMessageListener;
import com.evervoid.client.interfaces.EVGlobalMessageListener;
import com.evervoid.client.interfaces.EVLobbyMessageListener;
import com.evervoid.json.Json;
import com.evervoid.network.message.EverMessage;
import com.evervoid.network.message.EverMessageHandler;
import com.evervoid.network.message.EverMessageListener;
import com.evervoid.network.message.HandshakeMessage;
import com.evervoid.network.message.TurnMessage;
import com.evervoid.server.EVServerMessageObserver;
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

	private Client aClient;
	private final Set<EVGameMessageListener> aGameObservers;
	private final Set<EVGlobalMessageListener> aGlobalObservers;
	private final Set<EVLobbyMessageListener> aLobbyObservers;
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
		aGlobalObservers = new HashSet<EVGlobalMessageListener>();
		aGameObservers = new HashSet<EVGameMessageListener>();
		aLobbyObservers = new HashSet<EVLobbyMessageListener>();
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
		aMessageHandler.send(new HandshakeMessage());
		sConnectionLog.info("Client message sent to server.");
		// serverConnection.addMessageListener(this, EverMessage.class);
	}

	public void deregisterObserver(final EVServerMessageObserver observer)
	{
		aGlobalObservers.remove(observer);
	}

	@Override
	public void messageReceived(final EverMessage message)
	{
		sConnectionLog.info("Client received: " + message);
		final String messageType = message.getType();
		final Json messageContents = message.getJson();
		if (messageType.equals("gamestate")) {
			for (final EVGlobalMessageListener observer : aGlobalObservers) {
				observer.receivedGameState(new EVGameState(messageContents));
			}
		}
		else if (messageType.equals("turn")) {
			for (final EVGameMessageListener observer : aGameObservers) {
				observer.receivedTurn(new Turn(messageContents));
			}
		}
	}

	public void sendTurn(final Turn turn)
	{
		aMessageHandler.send(new TurnMessage(turn));
	}
}

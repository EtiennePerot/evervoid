package com.evervoid.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evervoid.json.Json;
import com.evervoid.network.EverMessage;
import com.evervoid.network.EverMessageHandler;
import com.evervoid.network.EverMessageListener;
import com.jme3.network.connection.Client;
import com.jme3.network.connection.Server;
import com.jme3.network.events.ConnectionListener;

// TODO Make this a singleton
/**
 * everVoid Server allowing communication from and to clients.
 */
public class EVServerEngine implements ConnectionListener, EverMessageListener
{
	private static EVServerEngine sInstance = null;
	public static final Logger sServerLog = Logger.getLogger(EVServerEngine.class.getName());

	public static EVServerEngine getInstance()
	{
		if (sInstance == null) {
			sInstance = new EVServerEngine();
		}
		return sInstance;
	}

	public static void registerListener(final EVServerMessageObserver listener)
	{
		sInstance.aObservers.add(listener);
	}

	private final List<Client> aClients;
	private final EverMessageHandler aMessageHandler;
	public final Set<EVServerMessageObserver> aObservers;
	private Server aSpiderMonkeyServer;
	private final int aTCPport;
	private final int aUDPport;

	/**
	 * Constructor for the EverVoidServer using default ports.
	 */
	private EVServerEngine()
	{
		this(51255, 51255);
	}

	/**
	 * Constructor with specified UDP and TCP ports.
	 * 
	 * @param pTCPport
	 *            TCP port to use.
	 * @param pUDPport
	 *            UDP port to use.
	 */
	private EVServerEngine(final int pTCPport, final int pUDPport)
	{
		sInstance = this;
		aObservers = new HashSet<EVServerMessageObserver>();
		aClients = new ArrayList<Client>();
		sServerLog.setLevel(Level.ALL);
		sServerLog.info("Creating server on ports " + pTCPport + "; " + pUDPport);
		aTCPport = pTCPport;
		aUDPport = pUDPport;
		try {
			aSpiderMonkeyServer = new Server(aTCPport, aUDPport);
		}
		catch (final IOException e) {
			sServerLog.severe("Could not initialise the server. Caught IOException.");
		}
		sServerLog.info("Server created: " + aSpiderMonkeyServer);
		aMessageHandler = new EverMessageHandler(aSpiderMonkeyServer);
		aMessageHandler.addMessageListener(this);
		aSpiderMonkeyServer.addConnectionListener(this);
		sServerLog.info("Set connection listener and message listener.");
		// By default, generate a random game state
		sServerLog.info("Server game state created.");
		try {
			aSpiderMonkeyServer.start();
		}
		catch (final IOException e) {
			sServerLog.info("Cannot start server: " + e.getStackTrace());
		}
		sServerLog.info("Server up and waiting for connections.");
	}

	@Override
	public void clientConnected(final Client client)
	{
		sServerLog.info("Client connected: " + client);
	}

	@Override
	public void clientDisconnected(final Client client)
	{
		sServerLog.info("Client disconnected: " + client);
	}

	public void deregisterObserver(final EVServerMessageObserver observer)
	{
		aObservers.remove(observer);
	}

	@Override
	public void messageReceived(final EverMessage message)
	{
		aClients.add(message.getClient());
		for (final EVServerMessageObserver observer : aObservers) {
			observer.messageReceived(message.getType(), message.getClient(), message.getJson());
		}
	}

	public void send(final Client client, final String messageType, final Json content)
	{
		aMessageHandler.send(client, new EverMessage(content, messageType));
	}

	public void sendAll(final String messageType, final Json content)
	{
		for (final Client client : aClients) {
			send(client, messageType, content);
		}
	}

	public void stop()
	{
		try {
			aSpiderMonkeyServer.stop();
		}
		catch (final IOException e) {
			sServerLog.severe("Could not stop the server. Caught IOException.");
		}
	}
}

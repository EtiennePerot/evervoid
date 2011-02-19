package com.evervoid.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evervoid.json.Json;
import com.evervoid.network.EVNetworkObserver;
import com.evervoid.network.message.EverMessage;
import com.evervoid.network.message.EverMessageHandler;
import com.evervoid.network.message.EverMessageListener;
import com.jme3.network.connection.Client;
import com.jme3.network.connection.Server;
import com.jme3.network.events.ConnectionListener;

// TODO Make this a singleton
/**
 * everVoid Server allowing communication from and to clients.
 */
public class EVServerEngine implements ConnectionListener, EverMessageListener
{
	public static final Logger sServerLog = Logger.getLogger(EVServerEngine.class.getName());

	public static void main(final String[] args)
	{
		new EVServerEngine();
	}

	private final EverMessageHandler aMessageHandler;
	public final Set<EVNetworkObserver> aObservers;
	private Server aSpiderMonkeyServer;
	private final int aTCPport;
	private final int aUDPport;

	/**
	 * Constructor for the EverVoidServer using default ports.
	 */
	public EVServerEngine()
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
	public EVServerEngine(final int pTCPport, final int pUDPport)
	{
		aObservers = new HashSet<EVNetworkObserver>();
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

	public void deregisterObserver(final EVNetworkObserver observer)
	{
		aObservers.remove(observer);
	}

	@Override
	public void messageReceived(final EverMessage message)
	{
		for (final EVNetworkObserver observer : aObservers) {
			observer.messageReceived(message.getType(), message.getClient(), message.getJson());
		}
		if (message.getType().equals("handshake")) {
			// aMessageHandler.send(message.getClient(), new GameStateMessage(aGameEngine.getGameState()));
		}
	}

	public void registerObserver(final EVNetworkObserver observer)
	{
		aObservers.add(observer);
	}

	public void send(final Client client, final String messageType, final Json content)
	{
		aMessageHandler.send(client, new EverMessage(content, messageType));
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

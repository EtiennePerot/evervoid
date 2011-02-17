package com.evervoid.network;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evervoid.state.EVGameState;
import com.jme3.network.connection.Client;
import com.jme3.network.connection.Server;
import com.jme3.network.events.ConnectionListener;

// TODO Make this a singleton
/**
 * everVoid Server allowing communication from and to clients.
 */
public class EverVoidServer implements ConnectionListener, EverMessageListener
{
	public static final Logger sServerLog = Logger.getLogger(EverVoidServer.class.getName());

	public static void main(final String[] args)
	{
		new EverVoidServer();
	}

	private Server aEvServer;
	private final EVGameState aGameState;
	private final EverMessageHandler aMessageHandler;
	private final int aTCPport;
	private final int aUDPport;

	/**
	 * Constructor for the EverVoidServer using default ports.
	 */
	public EverVoidServer()
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
	public EverVoidServer(final int pTCPport, final int pUDPport)
	{
		sServerLog.setLevel(Level.ALL);
		sServerLog.info("Creating server on ports " + pTCPport + "; " + pUDPport);
		aTCPport = pTCPport;
		aUDPport = pUDPport;
		try {
			aEvServer = new Server(aTCPport, aUDPport);
		}
		catch (final IOException e) {
			sServerLog.severe("Could not initialise the server. Caught IOException.");
		}
		sServerLog.info("Server created: " + aEvServer);
		aMessageHandler = new EverMessageHandler(aEvServer);
		aMessageHandler.addMessageListener(this);
		aEvServer.addConnectionListener(this);
		sServerLog.info("Set connection listener and message listener.");
		// By default, generate a random game state
		aGameState = new EVGameState();
		sServerLog.info("Server game state created.");
		try {
			aEvServer.start();
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

	@Override
	public void messageReceived(final EverMessage message)
	{
		System.out.println(message);
		System.out.println(message.getType());
		if (message.getType().equals("handshake")) {
			aMessageHandler.send(message.getClient(), new GameStateMessage(aGameState));
		}
	}

	public void stop()
	{
		try {
			aEvServer.stop();
		}
		catch (final IOException e) {
			sServerLog.severe("Could not stop the server. Caught IOException.");
		}
	}
}

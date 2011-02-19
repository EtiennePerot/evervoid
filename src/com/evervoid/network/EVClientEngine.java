package com.evervoid.network;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evervoid.json.Json;
import com.evervoid.network.message.EverMessage;
import com.evervoid.network.message.EverMessageHandler;
import com.evervoid.network.message.EverMessageListener;
import com.evervoid.network.message.HandshakeMessage;
import com.jme3.network.connection.Client;

public class EVClientEngine implements EverMessageListener
{
	public static final Logger sConnectionLog = Logger.getLogger(EVClientEngine.class.getName());
	private Client aClient;
	private final EverMessageHandler aMessageHandler;
	private final Set<EVNetworkObserver> aObservers;
	private final String aServerIP;
	private final int aTCPport;
	private final int aUDPport;

	/**
	 * Initialize a connection to a server using default ports.
	 * 
	 * @param pServerIP
	 *            Address of the server.
	 */
	public EVClientEngine(final String pServerIP)
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
		aObservers = new HashSet<EVNetworkObserver>();
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

	public void deregisterObserver(final EVNetworkObserver observer)
	{
		aObservers.remove(observer);
	}

	@Override
	public void messageReceived(final EverMessage message)
	{
		sConnectionLog.info("Client received: " + message);
		final Json data = message.getJson();
		for (final EVNetworkObserver observer : aObservers) {
			observer.messageReceived(message.getType(), null, message.getJson());
		}
	}

	public void registerObserver(final EVNetworkObserver observer)
	{
		aObservers.add(observer);
	}
}

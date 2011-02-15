package com.evervoid.network.connection;

import java.io.IOException;
import java.util.logging.Logger;

import com.evervoid.state.action.Turn;
import com.jme3.network.connection.Client;

public class ServerConnection
{
	public static final Logger connectionLog = Logger.getLogger(ServerConnection.class.getName());
	private final String fServerIP;
	private final int fTCPport;
	private final int fUDPport;
	private Client serverConnection;

	/**
	 * Initialise a connection to a server using default ports.
	 * 
	 * @param pServerIP
	 *            Address of the server.
	 */
	public ServerConnection(final String pServerIP)
	{
		this(pServerIP, 51255, 51256);
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
	 */
	public ServerConnection(final String pServerIP, final int pTCPport, final int pUDPport)
	{
		fServerIP = new String(pServerIP);
		fTCPport = pTCPport;
		fUDPport = pUDPport;
		try {
			serverConnection = new Client(fServerIP, fTCPport, fUDPport);
		}
		catch (final IOException e) {
			connectionLog.severe("Could not establish connection to server. IOException caught.");
		}
	}

	/**
	 * Sends a Turn to the server
	 * 
	 * @param turn
	 *            The Turn object to send
	 */
	public void sendTurn(final Turn turn)
	{
		try {
			serverConnection.send(new TurnMessage(turn));
		}
		catch (final IOException e) {
			connectionLog.severe("Could send turn " + turn + ". IOException caught.");
		}
	}

	/**
	 * Starts the connection to the server.
	 */
	public void start()
	{
		serverConnection.start();
	}
}

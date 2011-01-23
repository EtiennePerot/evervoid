package com.evervoid.network.server;

import java.io.IOException;
import java.util.logging.Logger;

import com.jme3.network.connection.Server;

// TODO Make this a singleton
/**
 * everVoid Server allowing communication from and to clients.
 */
public class EverVoidServer
{
	public static final Logger serverLog = Logger.getLogger(EverVoidServer.class.getName());
	private Server evServer;
	private final int fTCPport;
	private final int fUDPport;

	/**
	 * Default constructor for the EverVoidServer
	 */
	public EverVoidServer()
	{
		fTCPport = 51255;
		fUDPport = 51256;
		evServer = null;
	}

	/**
	 * Overloaded constructor with specified UDP and TCP ports.
	 * 
	 * @param pTCPport
	 *            Number of the TCP port to use
	 * @param pUDPport
	 *            Number of the UDP port to use
	 */
	public EverVoidServer(final int pTCPport, final int pUDPport)
	{
		fTCPport = pTCPport;
		fUDPport = pUDPport;
		evServer = null;
	}

	/**
	 * Initialise and start the server. Does nothing if the server is already running.
	 */
	public void start()
	{
		if (evServer == null) {
			try {
				evServer = new Server(fTCPport, fUDPport);
				evServer.start();
			}
			catch (final IOException e) {
				serverLog.severe("Could not initialise the server. Caught IOException.");
			}
		}
	}
}

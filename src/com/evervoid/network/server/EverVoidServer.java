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
	 * Constructor for the EverVoidServer using default ports.
	 */
	public EverVoidServer()
	{
		this(51255, 51256);
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
		fTCPport = pTCPport;
		fUDPport = pUDPport;
		try {
			evServer = new Server(fTCPport, fUDPport);
		}
		catch (final IOException e) {
			serverLog.severe("Could not initialise the server. Caught IOException.");
		}
	}

	/**
	 * Starts the server. Does nothing if the server is already running.
	 */
	public void start()
	{
		if (!evServer.isRunning()) {
			try {
				evServer.start();
			}
			catch (final IOException e) {
				serverLog.severe("Could not start the server. Caught IOException.");
			}
		}
	}

	/**
	 * Stops the server. Does nothing if the server is not already running.
	 */
	public void stop()
	{
		if (evServer.isRunning()) {
			try {
				evServer.stop();
			}
			catch (final IOException e) {
				serverLog.severe("Could not stop the server. Caught IOException.");
			}
		}
	}
}

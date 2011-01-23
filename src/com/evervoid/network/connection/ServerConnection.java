package com.evervoid.network.connection;

import java.io.IOException;
import java.util.logging.Logger;

import com.jme3.network.connection.Client;

public class ServerConnection
{
	public static final Logger connectionLog = Logger.getLogger(ServerConnection.class.getName());
	private final String fServerIP;
	private final int fTCPport;
	private final int fUDPport;
	private Client serverConnection;

	public ServerConnection(final String pServerIP)
	{
		fServerIP = new String(pServerIP);
		fTCPport = 51255;
		fUDPport = 51256;
		serverConnection = null;
	}

	public ServerConnection(final String pServerIP, final int pTCPport, final int pUDPport)
	{
		fServerIP = new String(pServerIP);
		fTCPport = pTCPport;
		fUDPport = pUDPport;
		serverConnection = null;
	}

	public void start()
	{
		if (serverConnection == null) {
			try {
				serverConnection = new Client(fServerIP, fTCPport, fUDPport);
				serverConnection.start();
			}
			catch (final IOException e) {
				connectionLog.severe("Could not establish connection to server");
			}
		}
	}
}

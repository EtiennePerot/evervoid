package com.evervoid.network;

import java.io.IOException;
import java.util.logging.Logger;

import com.evervoid.json.Json;
import com.jme3.network.connection.Client;
import com.jme3.network.events.MessageAdapter;
import com.jme3.network.serializing.Serializer;

public class NetworkEngine extends MessageAdapter
{
	public static final Logger sConnectionLog = Logger.getLogger(NetworkEngine.class.getName());
	private Client aServerConnection;
	private final String aServerIP;
	private final int aTCPport;
	private final int aUDPport;

	/**
	 * Initialize a connection to a server using default ports.
	 * 
	 * @param pServerIP
	 *            Address of the server.
	 */
	public NetworkEngine(final String pServerIP)
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
	 */
	public NetworkEngine(final String pServerIP, final int pTCPport, final int pUDPport)
	{
		aServerIP = new String(pServerIP);
		aTCPport = pTCPport;
		aUDPport = pUDPport;
		try {
			aServerConnection = new Client(aServerIP, aTCPport, aUDPport);
		}
		catch (final IOException e) {
			sConnectionLog.severe("Could not establish connection to server. IOException caught.");
		}
		aServerConnection.start();
		Serializer.registerClass(InnerMessage.class);
		Serializer.registerClass(EverCompressedMessage.class);
		try {
			aServerConnection.send(new EverMessage(new Json("ohai there"), "ohai").getMessage());
			System.out.println("Sent");
		}
		catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// serverConnection.addMessageListener(this, EverMessage.class);
	}
}

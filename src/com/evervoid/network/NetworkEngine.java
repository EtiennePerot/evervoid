package com.evervoid.network;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.network.connection.Client;
import com.jme3.network.events.MessageAdapter;
import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializer;

public class NetworkEngine extends MessageAdapter
{
	public static final Logger sConnectionLog = Logger.getLogger(NetworkEngine.class.getName());
	private Client aClient;
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
		// aClient.addMessageListener(this, EverMessage.class);
		Serializer.registerClass(InnerMessage.class);
		Serializer.registerClass(EverCompressedMessage.class);
		sConnectionLog.info("Message classes registered to client Serializer.");
		aClient.start();
		sConnectionLog.info("Client started.");
		/*
		 * try { Thread.sleep(5000); } catch (final InterruptedException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); }
		 */
		try {
			aClient.send(new Handshake().getMessage());
			sConnectionLog.info("Client message sent to server.");
		}
		catch (final IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
		// serverConnection.addMessageListener(this, EverMessage.class);
	}

	@Override
	public void messageReceived(final Message message)
	{
		final EverCompressedMessage msg = (EverCompressedMessage) message;
		if (msg.getType().equals("gamestate")) {
			// final GameStateMessage gmsg = (GameStateMessage) msg;
			// System.out.println("Got game state: " + gmsg.getGameState().toJson());
		}
	}
}

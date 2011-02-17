package com.evervoid.network;

import java.io.IOException;
import java.util.logging.Logger;

import com.evervoid.state.EVGameState;
import com.jme3.network.connection.Client;
import com.jme3.network.connection.Server;
import com.jme3.network.events.ConnectionListener;
import com.jme3.network.events.MessageAdapter;
import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializer;

// TODO Make this a singleton
/**
 * everVoid Server allowing communication from and to clients.
 */
public class EverVoidServer extends MessageAdapter implements ConnectionListener
{
	public static final Logger sServerLog = Logger.getLogger(EverVoidServer.class.getName());

	public static void main(final String[] args)
	{
		new EverVoidServer();
	}

	private Server aEvServer;
	private final EVGameState aGameState;
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
		aTCPport = pTCPport;
		aUDPport = pUDPport;
		try {
			aEvServer = new Server(aTCPport, aUDPport);
		}
		catch (final IOException e) {
			sServerLog.severe("Could not initialise the server. Caught IOException.");
		}
		aEvServer.addConnectionListener(this);
		aEvServer.addMessageListener(this, EverMessage.class);
		Serializer.registerClass(InnerMessage.class);
		Serializer.registerClass(EverMessage.class);
		// By default, generate a random game state
		aGameState = new EVGameState();
		try {
			aEvServer.start();
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void clientConnected(final Client client)
	{
		System.out.println("Client connected " + client);
	}

	@Override
	public void clientDisconnected(final Client client)
	{
		System.out.println("Client disconnected " + client);
	}

	@Override
	public void messageReceived(final Message message)
	{
		final EverMessage msg = (EverMessage) message;
		System.out.println("Msg: " + message);
		System.out.println("Server received: " + msg.getJson());
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

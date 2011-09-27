package com.evervoid.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.evervoid.client.EverVoidClient;
import com.evervoid.utils.LoggerUtils;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.base.DefaultClient;
import com.jme3.network.kernel.Connector;
import com.jme3.network.kernel.tcp.SocketConnector;
import com.jme3.network.kernel.udp.UdpConnector;

/**
 * The network client for everVoid. Receives partial messages from the server, compiles them into full EVMessages and warns all
 * listeners about the new messages. Objects wanting to be informed about new messages should register to the client as
 * {@link EVMessageListener}s.
 */
public class EVNetworkClient extends DefaultClient implements MessageListener<Client>
{
	/**
	 * All Objects listening for EVMessages
	 */
	private final Set<EVMessageListener> aListeners = new HashSet<EVMessageListener>();
	/**
	 * The partial messages received to be held until they can be built into a full EVMessage.
	 */
	private final Map<String, EVMessageBuilder> aMessages = new HashMap<String, EVMessageBuilder>();

	/**
	 * Connects to the default everVoid ports at the given host.
	 * 
	 * @param hostname
	 *            The hostname to connect to.
	 * @throws IOException
	 *             If the the host is unreachable or the ports are taken.
	 */
	public EVNetworkClient(final String hostname) throws IOException
	{
		this(hostname, EVNetworkServer.sDefaultPortTCP, EVNetworkServer.sDefaultPortUDP);
	}

	/**
	 * Connects at the given ports and host.
	 * 
	 * @param hostname
	 *            The hostname to connect to.
	 * @param tcpPort
	 *            The port on the remove host to which connect our TCP socket.
	 * @param udpPort
	 *            The port on the remove host to which connect our UPD socket.
	 * @throws IOException
	 *             If the the host is unreachable or the ports are taken.
	 */
	public EVNetworkClient(final String hostname, final int tcpPort, final int udpPort) throws IOException
	{
		super("everVoid", EverVoidClient.getVersionAsInt());
		if (!connect(hostname, tcpPort, udpPort)) {
			throw new IOException("Could not connect to host");
		}
		addMessageListener(this);
	}

	/**
	 * adds a listener that will be informed of all EVMessages received.
	 * 
	 * @param listener
	 *            The new listener.
	 */
	public void addEVMessageListener(final EVMessageListener listener)
	{
		aListeners.add(listener);
		LoggerUtils.info("Client has a new EverMessageListener: " + listener);
	}

	/**
	 * Attempts to connect UDP and TCP sockets and sets them if connection is sucessful.
	 * 
	 * @param hostname
	 *            The remote host.
	 * @param tcpPort
	 *            The port for the TCP socket.
	 * @param updPort
	 *            The port for the UDP socket.
	 * @return Whether the Sockets were successfully created.
	 */
	public boolean connect(final String hostname, final int tcpPort, final int updPort)
	{
		InetAddress address = null;
		// find the host
		try {
			address = InetAddress.getByName(hostname);
		}
		catch (final UnknownHostException e) {
			LoggerUtils.warning("No host found at" + hostname);
			e.printStackTrace();
			return false;
		}
		// create the TCP socket
		Connector tcpConnector = null;
		try {
			tcpConnector = new SocketConnector(address, tcpPort);
		}
		catch (final IOException e) {
			LoggerUtils.warning("Could not create tcp Connector to host " + hostname + " on port " + tcpPort);
			e.printStackTrace();
			return false;
		}
		// create the UPD port
		Connector udpConnector = null;
		try {
			udpConnector = new UdpConnector(address, updPort);
		}
		catch (final IOException e) {
			LoggerUtils.warning("Could not create upd Connector to host " + hostname + " on port " + updPort);
			e.printStackTrace();
			return false;
		}
		// everything worked, set connectors
		setConnectors(tcpConnector, udpConnector);
		return true;
	}

	/**
	 * Removes the partial message from the saved list.
	 * 
	 * @param message
	 *            The message to remove.
	 */
	private void removePartialMessage(final PartialMessage message)
	{
		aMessages.remove(message.getHash());
	}

	/**
	 * @param message
	 *            The message to get a builder for
	 * @return The builder associated with the given message
	 */
	private EVMessageBuilder getBuilder(final PartialMessage message)
	{
		final String hash = message.getHash();
		if (!aMessages.containsKey(hash)) {
			final EVMessageBuilder builder = new EVMessageBuilder(message.getType(), message.getTotalParts());
			aMessages.put(hash, builder);
			return builder;
		}
		return aMessages.get(hash);
	}

	@Override
	public void messageReceived(final Client source, final Message m)
	{
		final PartialMessage msg = (PartialMessage) m;
		LoggerUtils.info("Client side EverMessageHandler received a new PartialMessage from " + source + ": " + msg);
		final EVMessageBuilder builder = getBuilder(msg);
		builder.addPart(msg);
		final EVMessage finalMsg = builder.getMessage();
		if (finalMsg == null) {
			LoggerUtils.info("PartialMessage is not enough to complete the full EverMessage.");
			return;
		}
		LoggerUtils.info("PartialMessage is enough to complete the full EverMessage from " + source + ": " + finalMsg);
		removePartialMessage(msg);
		for (final EVMessageListener listener : aListeners) {
			listener.messageReceived(source, finalMsg);
		}
	}

	/**
	 * Sends an EVMessage asynchronously.
	 * 
	 * @param message
	 *            The message to send.
	 * @throws EVMessageSendingException
	 *             If the server could not be reached.
	 */
	public void sendEverMessage(final EVMessage message) throws EVMessageSendingException
	{
		// send() will always return true when sending asycnhronously, might as well drop it
		sendEverMessage(message, true);
	}

	/**
	 * Sends an EVMessage.
	 * 
	 * @param message
	 *            The message to send.
	 * @param async
	 *            Whether to send the message asynchronously
	 * @throws EVMessageSendingException
	 *             If the server could not be reached.
	 */
	public void sendEverMessage(final EVMessage message, final boolean async) throws EVMessageSendingException
	{
		final Postman postman = new Postman(this, message);
		if (async) {
			postman.start();
			return;
		}
		postman.send();
	}
}

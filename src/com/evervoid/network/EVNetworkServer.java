package com.evervoid.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.evervoid.client.EverVoidClient;
import com.evervoid.utils.LoggerUtils;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.base.DefaultServer;
import com.jme3.network.kernel.tcp.SelectorKernel;
import com.jme3.network.kernel.udp.UdpKernel;
import com.jme3.network.serializing.Serializer;

/**
 * A network server for everVoid games. Connections are represented by {@link HostedConnection}s. Received
 * {@link PartialMessage}s are stored until they can be compiled into {@link EVMessage}s and sent to all the
 * {@link EVMessageListener}s.
 */
public class EVNetworkServer extends DefaultServer implements MessageListener<HostedConnection>
{
	/**
	 * The default everVoid port for TCP communication.
	 */
	public static final int sDefaultPortTCP = 51255;
	/**
	 * The default everVoid port for UPD communication.
	 */
	public static final int sDefaultPortUDP = 51255;
	/**
	 * The default everVoid port for TCP discovery.
	 */
	public static final int sDiscoveryPortTCP = 51257;
	/**
	 * The default everVoid port for TCP discovery.
	 */
	public static final int sDiscoveryPortUDP = 51258;
	/**
	 * All objects listening for EVMessages delivered to the server.
	 */
	private final Set<EVMessageListener> aListeners = new HashSet<EVMessageListener>();
	/**
	 * Incomplete EVMessages waiting for more PartialMessages.
	 */
	private final Map<String, EVMessageBuilder> aMessages = new HashMap<String, EVMessageBuilder>();

	/**
	 * Creates a server on the default everVoid ports.
	 * 
	 * @throws IOException
	 *             If the server fails to grab the ports.
	 */
	public EVNetworkServer() throws IOException
	{
		this(sDefaultPortTCP, sDefaultPortUDP);
	}

	/**
	 * Creates a server on the given ports.
	 * 
	 * @param tcpPort
	 *            The port to use for TCP communication.
	 * @param updPort
	 *            The port to use for UDP communication.
	 * @throws IOException
	 *             If the server fails to grab the ports.
	 */
	public EVNetworkServer(final int tcpPort, final int updPort) throws IOException
	{
		super("everVoid", EverVoidClient.getVersionAsInt(), new SelectorKernel(tcpPort), new UdpKernel(updPort));
		Serializer.registerClass(PartialMessage.class);
		addMessageListener(this);
	}

	/**
	 * Registers the listener for all incoming EVMessages.
	 * 
	 * @param listener
	 *            The listener.
	 */
	public void addEVMessageListener(final EVMessageListener listener)
	{
		aListeners.add(listener);
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
	public void messageReceived(final HostedConnection source, final Message m)
	{
		final PartialMessage msg = (PartialMessage) m;
		LoggerUtils.info("Server received a new PartialMessage from " + source + ": " + msg);
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
	 * Sends an EVMessage to the connection.
	 * 
	 * @param destination
	 *            The message destination.
	 * @param message
	 *            The message to send.
	 * @param async
	 *            Whether sending should be asynchronous.
	 * @throws EVMessageSendingException
	 *             If the destination cannot be reached. Asynchronous messages will not raise exceptions.
	 */
	public void sendEVMessage(final HostedConnection destination, final EVMessage message, final boolean async)
			throws EVMessageSendingException
	{
		final Postman postman = new Postman(destination, message);
		if (async) {
			postman.start();
			return;
		}
		postman.send();
	}
}

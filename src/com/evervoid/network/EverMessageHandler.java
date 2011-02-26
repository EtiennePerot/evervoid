package com.evervoid.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.network.connection.Client;
import com.jme3.network.connection.Server;
import com.jme3.network.events.MessageAdapter;
import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializer;

/**
 * Handles deconstruction and reconstruction of EverMessages.
 */
public class EverMessageHandler extends MessageAdapter
{
	private static final Logger sPartialMessageLogger = Logger.getLogger(EverMessageHandler.class.getName());
	private Client aClient = null;
	private final boolean aClientSide;
	private final Set<EverMessageListener> aListeners = new HashSet<EverMessageListener>();
	private final Map<String, EverMessageBuilder> aMessages = new HashMap<String, EverMessageBuilder>();

	public EverMessageHandler(final Client client)
	{
		sPartialMessageLogger.setLevel(Level.ALL);
		aClientSide = true;
		aClient = client;
		sPartialMessageLogger.info(getSide() + "EverMessageHandler initializing");
		Serializer.registerClass(ByteMessage.class);
		Serializer.registerClass(PartialMessage.class);
		client.addMessageListener(this, PartialMessage.class);
	}

	public EverMessageHandler(final Server server)
	{
		sPartialMessageLogger.setLevel(Level.ALL);
		aClientSide = false;
		sPartialMessageLogger.info(getSide() + "EverMessageHandler initializing");
		Serializer.registerClass(ByteMessage.class);
		Serializer.registerClass(PartialMessage.class);
		server.addMessageListener(this, PartialMessage.class);
	}

	public void addMessageListener(final EverMessageListener listener)
	{
		aListeners.add(listener);
		sPartialMessageLogger.info(getSide() + "EverMessageHandler has a new EverMessageListener: " + listener);
	}

	private void deleteBuilder(final PartialMessage message)
	{
		aMessages.remove(message.getHash());
	}

	/**
	 * @param message
	 *            The message to get a builder for
	 * @return The builder associated with the given message
	 */
	private EverMessageBuilder getBuilder(final PartialMessage message)
	{
		final String hash = message.getHash();
		if (!aMessages.containsKey(hash)) {
			final EverMessageBuilder builder = new EverMessageBuilder(message.getType(), message.getTotalParts());
			aMessages.put(hash, builder);
			return builder;
		}
		return aMessages.get(hash);
	}

	/**
	 * Just there to help logging
	 * 
	 * @return "Client-side" or "Server-side"
	 */
	private String getSide()
	{
		if (aClientSide) {
			return "Client-side ";
		}
		return "Server-side ";
	}

	@Override
	public void messageReceived(final Message message)
	{
		final PartialMessage msg = (PartialMessage) message;
		sPartialMessageLogger.info(getSide() + "EverMessageHandler received a new PartialMessage: " + msg);
		final EverMessageBuilder builder = getBuilder(msg);
		builder.addPart(msg);
		final EverMessage finalMsg = builder.getMessage();
		if (finalMsg == null) {
			sPartialMessageLogger.info("PartialMessage is not enough to complete the full EverMessage.");
			return;
		}
		sPartialMessageLogger.info("PartialMessage is enough to complete the full EverMessage: " + finalMsg);
		deleteBuilder(msg);
		for (final EverMessageListener listener : aListeners) {
			listener.messageReceived(finalMsg);
		}
	}

	/**
	 * (Server-side) Split and send an EverMessage to a Client
	 * 
	 * @param destination
	 *            The client to send to
	 * @param message
	 *            The EverMessage to send
	 * @return True on success, false on failure
	 */
	public boolean send(final Client destination, final EverMessage message)
	{
		final List<PartialMessage> messages = message.getMessages();
		for (final PartialMessage part : messages) {
			try {
				if (destination != null) {
					destination.send(part);
				}
				else {
					return false;
				}
			}
			catch (final IOException e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * (Client-side) Split and send an EverMessage to the server
	 * 
	 * @param message
	 *            The message to send
	 * @return True on success, false on failure
	 */
	public boolean send(final EverMessage message)
	{
		return send(aClient, message);
	}
}

package com.evervoid.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evervoid.server.EverMessageSendingException;
import com.evervoid.utils.LoggerUtils;
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
	private class Postman extends Thread
	{
		private final Client aDestination;
		private final EverMessage aMessage;

		private Postman(final Client destination, final EverMessage message)
		{
			aDestination = destination;
			aMessage = message;
			setName("Postman");
		}

		@Override
		public void run()
		{
			try {
				send();
			}
			catch (final EverMessageSendingException e) {
				// Do nothing, can't notify back anymore at this point
			}
		}

		private boolean send() throws EverMessageSendingException
		{
			final List<PartialMessage> messages = aMessage.getMessages();
			int partIndex = 1;
			final int parts = messages.size();
			for (final PartialMessage part : messages) {
				try {
					LoggerUtils.info(getSide() + "EverMessageHandler sending to " + aDestination + ", part " + partIndex + "/"
							+ parts);
					aDestination.send(part);
					if (partIndex != parts) { // If it's not the last message
						Thread.sleep(100); // Wait a bit
					}
				}
				catch (final IOException e) {
					throw new EverMessageSendingException(aDestination);
				}
				catch (final NullPointerException e) {
					// Happens when inner client (inside the jME classes) doesn't get removed properly.
					throw new EverMessageSendingException(aDestination);
				}
				catch (final InterruptedException e) {
					// Not going to happen
				}
				partIndex++;
			}
			return true;
		}
	}

	private Client aClient = null;
	private final boolean aClientSide;
	private final Set<EverMessageListener> aListeners = new HashSet<EverMessageListener>();
	private final Map<String, EverMessageBuilder> aMessages = new HashMap<String, EverMessageBuilder>();

	public EverMessageHandler(final Client client)
	{
		aClientSide = true;
		aClient = client;
		LoggerUtils.info(getSide() + "EverMessageHandler initializing");
		Serializer.registerClass(ByteMessage.class);
		Serializer.registerClass(PartialMessage.class);
		client.addMessageListener(this, PartialMessage.class);
	}

	public EverMessageHandler(final Server server)
	{
		aClientSide = false;
		LoggerUtils.info(getSide() + "EverMessageHandler initializing");
		Serializer.registerClass(ByteMessage.class);
		Serializer.registerClass(PartialMessage.class);
		server.addMessageListener(this, PartialMessage.class);
	}

	public void addMessageListener(final EverMessageListener listener)
	{
		aListeners.add(listener);
		LoggerUtils.info(getSide() + "EverMessageHandler has a new EverMessageListener: " + listener);
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
		LoggerUtils.info(getSide() + "EverMessageHandler received a new PartialMessage from " + message.getClient() + ": "
				+ msg);
		final EverMessageBuilder builder = getBuilder(msg);
		builder.addPart(msg);
		final EverMessage finalMsg = builder.getMessage();
		if (finalMsg == null) {
			LoggerUtils.info("PartialMessage is not enough to complete the full EverMessage.");
			return;
		}
		LoggerUtils.info("PartialMessage is enough to complete the full EverMessage from " + message.getClient() + ": "
				+ finalMsg);
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
	 * @throws EverMessageSendingException
	 *             When message sending fails
	 */
	public boolean send(final Client destination, final EverMessage message) throws EverMessageSendingException
	{
		return send(destination, message, false);
	}

	/**
	 * (Server-side) Split and send an EverMessage to a Client
	 * 
	 * @param destination
	 *            The client to send to
	 * @param message
	 *            The EverMessage to send
	 * @param async
	 *            Whether to send asynchronously or not. If true, exceptions will not be thrown.
	 * @return True on success or when asynchronous, false on failure
	 * @throws EverMessageSendingException
	 *             When message sending fails
	 */
	public boolean send(final Client destination, final EverMessage message, final boolean async)
			throws EverMessageSendingException
	{
		final Postman postman = new Postman(destination, message);
		if (async) {
			postman.start();
			return true;
		}
		return postman.send();
	}

	/**
	 * (Client-side) Split and send an EverMessage to the server
	 * 
	 * @param message
	 *            The message to send
	 * @return True on success, false on failure
	 * @throws EverMessageSendingException
	 *             When message fails to deliver
	 */
	public boolean send(final EverMessage message) throws EverMessageSendingException
	{
		return send(message, false);
	}

	/**
	 * (Client-side) Split and send an EverMessage to the server
	 * 
	 * @param message
	 *            The message to send
	 * @param async
	 *            Whether to send asynchronously or not. If true, exceptions will not be thrown.
	 * @return True on success, false on failure
	 * @throws EverMessageSendingException
	 *             When message fails to deliver
	 */
	public boolean send(final EverMessage message, final boolean async) throws EverMessageSendingException
	{
		return send(aClient, message, async);
	}
}

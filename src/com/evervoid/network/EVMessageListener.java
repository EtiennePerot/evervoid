package com.evervoid.network;

import com.jme3.network.Client;
import com.jme3.network.HostedConnection;
import com.jme3.network.MessageConnection;

/**
 * Class wishing to be notified about incoming {@link EVMessage}s. These can come rom an {@link EVNetworkClient} or an
 * {@link EVNetworkServer}. In the client case, the {@link MessageConnection} will be a {@link Client}. For the server it will
 * be a {@link HostedConnection} determining which connection the message came from. Only EVMessages will be sent.
 */
public interface EVMessageListener
{
	/**
	 * Called when an {@link EVMessage} is received.
	 * 
	 * @param source
	 *            Where the message came from.
	 * @param message
	 *            The received message.
	 */
	public void messageReceived(MessageConnection source, EVMessage message);
}

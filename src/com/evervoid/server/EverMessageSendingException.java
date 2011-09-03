package com.evervoid.server;

import com.jme3.network.connection.Client;

/**
 * An Exception thrown when a Server fails to send a message.
 */
public class EverMessageSendingException extends Exception
{
	/**
	 * Serial ID for version reasons
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The Client whom the Server was trying to contact when the error occurred
	 */
	private final Client aClient;

	/**
	 * Creates a new {@link EverMessageSendingException} and binds it to the Client causing it
	 * 
	 * @param client
	 *            The client causing the exception
	 */
	public EverMessageSendingException(final Client client)
	{
		aClient = client;
	}

	/**
	 * @return The Client
	 */
	public Client getClient()
	{
		return aClient;
	}
}

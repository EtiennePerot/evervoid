package com.evervoid.network;

import com.jme3.network.MessageConnection;

/**
 * An Exception thrown when a Server fails to send a message.
 */
public class EVMessageSendingException extends Exception
{
	/**
	 * Serial ID for version reasons
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The Client whom the Server was trying to contact when the error occurred
	 */
	private final MessageConnection aClient;

	/**
	 * Creates a new {@link EVMessageSendingException} and binds it to the Client causing it
	 * 
	 * @param destination
	 *            The client causing the exception
	 */
	public EVMessageSendingException(final MessageConnection destination)
	{
		aClient = destination;
	}

	/**
	 * @return The Client
	 */
	public MessageConnection getClient()
	{
		return aClient;
	}
}

package com.evervoid.utils;

/**
 * Thrown when a creator fails to initialize correctly.
 */
public class BadInitialization extends Exception
{
	/**
	 * Default serial id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exception with the given message.
	 * 
	 * @param pMessage
	 *            The message to display to the user.
	 */
	public BadInitialization(final String pMessage)
	{
		super(pMessage);
	}
}

package com.evervoid.state.action;

import com.evervoid.state.EVGameState;

/**
 * This Error is thrown when something attempts to execute an invalid {@link Action} on its underlying {@link EVGameState}. It
 * is a sign that the Action is well built, but is preconditions are not met by the state on which it should execute.
 */
public class IllegalEVActionException extends Exception
{
	/**
	 * For versioning purposes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor with an error message to be displayed to stderr.
	 * 
	 * @param message
	 *            The error message.
	 */
	public IllegalEVActionException(final String message)
	{
		super(message);
	}
}

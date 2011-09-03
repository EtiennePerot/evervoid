package com.evervoid.state;

/**
 * This Error is thrown when the a user attempts to load a bad Json save file.
 */
public class BadSaveFileException extends Exception
{
	/**
	 * for versioning purposes
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a BadSaveFileExpection with the given error message
	 * 
	 * @param errorMessage
	 *            The message to be displayed upon logging of the error
	 */
	public BadSaveFileException(final String errorMessage)
	{
		super(errorMessage);
	}
}

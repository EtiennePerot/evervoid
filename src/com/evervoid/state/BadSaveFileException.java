package com.evervoid.state;

/**
 * This Error is thrown when the a user attemps to load a bad Json save file.
 */
public class BadSaveFileException extends Exception
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a BadSaveFileExpection with the given error message
	 */
	public BadSaveFileException(final String errorMessage)
	{
		super(errorMessage);
	}
}

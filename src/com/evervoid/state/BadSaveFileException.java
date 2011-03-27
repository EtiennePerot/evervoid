package com.evervoid.state;

public class BadSaveFileException extends Exception
{
	private static final long serialVersionUID = 1L;

	public BadSaveFileException(final String errorMessage)
	{
		super(errorMessage);
	}
}

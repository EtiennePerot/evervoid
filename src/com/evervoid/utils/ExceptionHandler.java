package com.evervoid.utils;

/**
 * An extension of {@link Runnable} that holds an {@link Exception}. This is used in {@link CatchableThread} to allow the
 * handler to know what caused the thread to throw an exception.
 */
public abstract class ExceptionHandler implements Runnable
{
	/**
	 * The Exception that caused the handler to be called.
	 */
	protected Exception aException;

	@Override
	public abstract void run();

	/**
	 * sets the local Exception that will then be used in the handler to determine what to do about the error.
	 * 
	 * @param pException
	 *            The Exception to set.
	 */
	public void setException(final Exception pException)
	{
		aException = pException;
	}
}

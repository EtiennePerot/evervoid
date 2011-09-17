package com.evervoid.utils;

/**
 * CatchableThread is a {@link Thread} that runs an {@link ExceptionHandler} when an {@link Exception} is caught during its
 * execution.
 */
public class CatchableThread extends Thread
{
	/**
	 * The callback to run when an error occurs.
	 */
	private ExceptionHandler aExceptionHandler;

	/**
	 * @param pRunnable
	 *            The runnable to execute.
	 * @param pExceptionHandler
	 *            The callback to run when an error is caught.
	 */
	public CatchableThread(final Runnable pRunnable, final ExceptionHandler pExceptionHandler)
	{
		super(pRunnable);
		aExceptionHandler = pExceptionHandler;
	}

	/**
	 * @param pRunnable
	 *            The runnable to execute.
	 * @param pName
	 *            The title of the Thread.
	 * @param pExceptionHandler
	 *            The callback to run when an error is caught.
	 */
	public CatchableThread(final Runnable pRunnable, final String pName, final ExceptionHandler pExceptionHandler)
	{
		super(pRunnable, pName);
		aExceptionHandler = pExceptionHandler;
	}

	@Override
	public void run()
	{
		if (aExceptionHandler != null) {
			try {
				super.run();
			}
			catch (final Exception e) {
				aExceptionHandler.setException(e);
				aExceptionHandler.run();
			}
		}
		else {
			// there is no handler, just run normally and throw any errors that come up
			super.run();
		}
	}

	/**
	 * Sets the exception handler to be run when an exception is caught.
	 * 
	 * @param pExceptionHandler
	 *            the callback to run.
	 */
	public void setHandler(final ExceptionHandler pExceptionHandler)
	{
		aExceptionHandler = pExceptionHandler;
	}
}

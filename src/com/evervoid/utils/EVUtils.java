package com.evervoid.utils;

/**
 * A static utility Class that groups functions useful to all everVoid classes.
 */
public class EVUtils
{
	/**
	 * The default display exception handler for everVoid.
	 */
	public static final ExceptionHandler sDefaultHandler = new ExceptionHandler()
	{
		@Override
		public void run()
		{
			LoggerUtils.warning("Display Thread got an error of type " + aException.getClass() + aException.getMessage());
			aException.printStackTrace();
		}
	};

	/**
	 * Runs a Runnable if it is not null. Avoids those annoying null checks everywhere.
	 * 
	 * @param callback
	 *            The Runnable to run.
	 */
	public static void runCallback(final Runnable callback)
	{
		if (callback != null) {
			callback.run();
		}
	}
}

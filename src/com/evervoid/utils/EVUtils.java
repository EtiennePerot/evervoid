package com.evervoid.utils;

public class EVUtils
{
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

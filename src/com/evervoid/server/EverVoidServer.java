package com.evervoid.server;

import com.evervoid.utils.LoggerUtils;

/**
 * Runs the standalone server for an everVoid game; the server is a singleton instance.
 */
public class EverVoidServer
{
	/**
	 * The singleton instance of the server.
	 */
	private static EverVoidServer sInstance;

	/**
	 * Ensures that the network engine is correctly started.
	 */
	public static void ensureStarted()
	{
		if (getInstance().aNetworkEngine == null) {
			sInstance.restart();
		}
	}

	/**
	 * @return The singleton instance of the server.
	 */
	public static EverVoidServer getInstance()
	{
		if (sInstance == null) {
			sInstance = new EverVoidServer();
		}
		return sInstance;
	}

	/**
	 * Stops the local network engine.
	 */
	public static void stop()
	{
		try {
			if (sInstance != null && sInstance.aNetworkEngine != null) {
				sInstance.aNetworkEngine.stop();
				sInstance.aNetworkEngine = null;
			}
		}
		catch (final Exception e) {
			LoggerUtils.warning("Caught Error in stoping server, bailing out\n" + e.getMessage() + "\n" + e.getStackTrace());
		}
	}

	/**
	 * The network engine instance.
	 */
	private EVNetworkEngine aNetworkEngine;

	/**
	 * The private constructor.
	 */
	private EverVoidServer()
	{
		aNetworkEngine = new EVNetworkEngine();
	}

	/**
	 * Stops the local network server and starts a new instance.
	 */
	public void restart()
	{
		stop();
		aNetworkEngine = new EVNetworkEngine();
	}
}

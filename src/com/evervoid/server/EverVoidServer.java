package com.evervoid.server;

public class EverVoidServer
{
	private static EverVoidServer sInstance;

	public static EverVoidServer getInstance()
	{
		if (sInstance == null) {
			sInstance = new EverVoidServer();
		}
		return sInstance;
	}

	public static void main(final String[] args)
	{
		new EverVoidServer();
	}

	private final EVServerEngine aNetworkEngine;

	private EverVoidServer()
	{
		// Don't make EVServerEngine a forced singleton; not being able to recreate it means not being able to restart the
		// server.
		aNetworkEngine = new EVServerEngine();
	}

	public void stop()
	{
		aNetworkEngine.stop();
		sInstance = null;
	}
}

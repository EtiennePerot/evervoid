package com.evervoid.server;

public class EverVoidServer
{
	public static final int sDiscoveryPortTCP = 51257;
	public static final int sDiscoveryPortUDP = 51258;
	public static final int sGamePortTCP = 51255;
	public static final int sGamePortUDP = 51255;
	private static EverVoidServer sInstance;

	public static void ensureStarted()
	{
		if (getInstance().aNetworkEngine == null) {
			sInstance.restart();
		}
	}

	public static EverVoidServer getInstance()
	{
		if (sInstance == null) {
			sInstance = new EverVoidServer();
		}
		return sInstance;
	}

	public static void main(final String[] args)
	{
		getInstance();
	}

	public static void stop()
	{
		if (sInstance != null && sInstance.aNetworkEngine != null) {
			sInstance.aNetworkEngine.stop();
			sInstance.aNetworkEngine = null;
		}
	}

	private EVServerEngine aNetworkEngine;

	private EverVoidServer()
	{
		aNetworkEngine = new EVServerEngine();
	}

	public void restart()
	{
		stop();
		aNetworkEngine = new EVServerEngine();
	}
}

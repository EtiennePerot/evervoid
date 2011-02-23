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

	private final EVGameEngine sGameEngine;
	private final EVServerEngine sNetworkEngine;

	private EverVoidServer()
	{
		sNetworkEngine = EVServerEngine.getInstance();
		sGameEngine = EVGameEngine.getInstance();
	}

	public void stop()
	{
		sNetworkEngine.stop();
	}
}

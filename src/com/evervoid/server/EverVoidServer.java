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
	private final EVServerEngine sNetowrkEngine;

	private EverVoidServer()
	{
		sNetowrkEngine = new EVServerEngine();
		sGameEngine = EVGameEngine.getInstance();
	}
}

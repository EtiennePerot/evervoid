package com.evervoid.server;

import com.evervoid.state.EVGameState;

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
		sGameEngine.setState(new EVGameState());
	}

	public void stop()
	{
		sNetworkEngine.stop();
	}
}

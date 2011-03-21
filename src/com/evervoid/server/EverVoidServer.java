package com.evervoid.server;

import com.evervoid.state.data.BadJsonInitialization;

public class EverVoidServer
{
	private static EverVoidServer sInstance;

	public static EverVoidServer getInstance()
	{
		if (sInstance == null) {
			try {
				sInstance = new EverVoidServer();
			}
			catch (final BadJsonInitialization e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sInstance;
	}

	public static void main(final String[] args)
	{
		try {
			new EverVoidServer();
		}
		catch (final BadJsonInitialization e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private final EVServerEngine aNetworkEngine;

	private EverVoidServer() throws BadJsonInitialization
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

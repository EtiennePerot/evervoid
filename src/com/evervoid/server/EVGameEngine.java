package com.evervoid.server;

import com.evervoid.state.EVGameState;

public class EVGameEngine
{
	private static EVGameEngine sInstance;
	private EVGameState aState;

	private EVGameEngine()
	{
		sInstance = this;
	}

	protected EVGameEngine getInstance()
	{
		if (sInstance == null) {
			sInstance = new EVGameEngine();
		}
		return sInstance;
	}

	protected void setState(final EVGameState state)
	{
		aState = state;
	}
}

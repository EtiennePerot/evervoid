package com.evervoid.network;

import com.evervoid.state.EVGameState;

public class LoadGameRequest extends EverMessage
{
	public LoadGameRequest(final EVGameState savedState)
	{
		super(savedState);
	}
}

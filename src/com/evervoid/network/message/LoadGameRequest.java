package com.evervoid.network.message;

import com.evervoid.network.EVMessage;
import com.evervoid.state.EVGameState;

public class LoadGameRequest extends EVMessage
{
	public LoadGameRequest(final EVGameState savedState)
	{
		super(savedState);
	}
}

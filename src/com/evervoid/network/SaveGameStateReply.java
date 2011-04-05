package com.evervoid.network;

import com.evervoid.state.EVGameState;

public class SaveGameStateReply extends EverMessage
{
	public SaveGameStateReply(final EVGameState savable)
	{
		super(savable);
	}
}

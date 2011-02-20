package com.evervoid.network.message;

import com.evervoid.state.action.Turn;

public class TurnMessage extends EverMessage
{
	public TurnMessage(final Turn turn)
	{
		super(turn, "turn");
	}
}

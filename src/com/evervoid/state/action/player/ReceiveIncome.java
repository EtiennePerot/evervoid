package com.evervoid.state.action.player;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;

public class ReceiveIncome extends PlayerAction
{
	public ReceiveIncome(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isValid()
	{
		// TODO Auto-generated method stub
		return false;
	}
}

package com.evervoid.state.action.research;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;

public class IncrementResearch extends ResearchAction
{
	public IncrementResearch(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void executeAction()
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean isValidResearchAction()
	{
		// TODO Auto-generated method stub
		return false;
	}
}

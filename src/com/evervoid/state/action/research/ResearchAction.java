package com.evervoid.state.action.research;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Action;
import com.evervoid.state.action.IllegalEVActionException;

public abstract class ResearchAction extends Action
{
	public ResearchAction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		// TODO Auto-generated constructor stub
	}
}

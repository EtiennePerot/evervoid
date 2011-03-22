package com.evervoid.state.action.research;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Action;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.player.Research;

public abstract class ResearchAction extends Action
{
	private Research aResearch;

	public ResearchAction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Check if this ResearchAction is valid. Calls the template method isValidResearchAction iff research is valid in the first
	 * place. Subclasses should only override isValidResearchAction, hence the "final" keyword on this method.
	 */
	@Override
	protected final boolean isValidAction()
	{
		// TODO: Check if research is valid instead of just "true"
		return true && isValidResearchAction();
	}

	protected abstract boolean isValidResearchAction();
}

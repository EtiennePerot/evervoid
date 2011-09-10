package com.evervoid.state.action.research;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Action;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.player.Research;

/**
 * Research is an abstract subclass of Action which wraps functionality common to all Actions pertaining to research.
 */
public abstract class ResearchAction extends Action
{
	/**
	 * The Research pertinent to this ResearchAction.
	 */
	private final Research aResearch;

	/**
	 * Json deserializing constructor. WARNING - currently creates an empty Research without deserializing
	 * 
	 * @param j
	 *            The Json to deserialize.
	 * @param state
	 *            The state on which to execute this action.
	 * @throws IllegalEVActionException
	 *             If the Json is malformed.
	 */
	public ResearchAction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		// FIXME - build based on json
		aResearch = new Research();
	}

	/**
	 * Check if this ResearchAction is valid. Calls the template method isValidResearchAction iff research is valid in the first
	 * place. Subclasses wishing to determine when they are valid should only override isValidResearchAction.
	 */
	@Override
	protected final boolean isValidAction()
	{
		// TODO: Check if research is valid instead of just "true"
		return true && isValidResearchAction();
	}

	/**
	 * This method determines whether the instance is a valid ResearchAction; it will only been called if the instance has
	 * already been determined to be a valid Action.
	 * 
	 * @return Whether this ResearchActoin is valid, as determined by its conditions on the state.
	 */
	protected abstract boolean isValidResearchAction();

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("research", aResearch);
		return j;
	}
}

package com.evervoid.state.action.research;

/**
 * IncrementResearch takes a Research object already in the process of being attained, and increments the current progress on it
 * by one turn.
 */
import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.Research;

/**
 * IncrementReserach increases the progress of a {@link Player} towards obtaining a single {@link Research}. The Player must
 * have enough resources to pay for the increment and must meet the prerequisites. The action deals with removing the correct
 * sum from the Player's resource pool.
 */
public class IncrementResearch extends ResearchAction
{
	/**
	 * @param j
	 *            The Json to deserialize.
	 * @param state
	 *            The state on which to execute this action.
	 * @throws IllegalEVActionException
	 *             If the Json is malformed.
	 */
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
	public String getDescription()
	{
		return null;
	}

	@Override
	protected boolean isValidResearchAction()
	{
		// TODO Auto-generated method stub
		return false;
	}
}

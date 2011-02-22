package com.evervoid.state.action;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.ship.JumpShip;
import com.evervoid.state.action.ship.MoveShip;

/**
 * Represents a game turn. Holds a list of actions.
 */
public class Turn implements Jsonable
{
	/**
	 * The list of actions played during this turn
	 */
	private final List<Action> aActions = new ArrayList<Action>();

	/**
	 * Constructor; does nothing right now...
	 */
	public Turn()
	{
		// Nothing
	}

	/**
	 * JSON constructor; rebuilds a Turn instance from a Json object
	 * 
	 * @param j
	 *            The Json representation of a Turn
	 */
	public Turn(final Json j, final EVGameState state)
	{
		for (final Json action : j.getListAttribute("turns")) {
			final String type = action.getStringAttribute("actiontype");
			if (type.equals("MoveShip")) {
				aActions.add(new MoveShip(action, state));
			}
			else if (type.equals("JumpShip")) {
				aActions.add(new JumpShip(action, state));
			}
		}
	}

	/**
	 * Adds an action to the list of actions during this turn
	 * 
	 * @param action
	 *            The action to add
	 */
	public void addAction(final Action action)
	{
		aActions.add(action);
	}

	public List<Action> getActions()
	{
		return aActions;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setListAttribute("turns", aActions);
		return j;
	}
}

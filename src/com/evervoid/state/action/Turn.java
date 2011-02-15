package com.evervoid.state.action;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

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
	public Turn(final Json j)
	{
		// TODO: Do something
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

	@Override
	public Json toJson()
	{
		return new Json(aActions);
	}
}

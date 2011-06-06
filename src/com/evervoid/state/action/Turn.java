package com.evervoid.state.action;

import static com.evervoid.state.action.Action.deserializeAction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVGameState;
import com.evervoid.utils.LoggerUtils;

/**
 * Represents a game turn. Holds a list of actions.
 */
public class Turn implements Jsonable, Iterable<Action>
{
	/**
	 * The list of actions played during this turn
	 */
	private final List<Action> aActions = new ArrayList<Action>();

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
			try {
				aActions.add(deserializeAction(state, action));
			}
			catch (final Exception e) {
				if (e instanceof IllegalEVActionException) {
					LoggerUtils.severe("Got an illegal action serialization while constructing turn");
				}
				else {
					LoggerUtils
							.severe("Trying to instantiate an Action that does not exists. Maybe Server version out of sync with Client?");
				}
				e.printStackTrace();
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

	private Turn addActions(final List<Action> actions)
	{
		for (final Action a : actions) {
			addAction(a);
		}
		return this;
	}

	public void addTurn(final Turn turn)
	{
		if (turn == null) {
			return;
		}
		for (final Action a : turn.getActions()) {
			addAction(a);
		}
	}

	@Override
	public Turn clone()
	{
		final Turn newTurn = new Turn();
		newTurn.aActions.addAll(aActions);
		return newTurn;
	}

	public void delAction(final Action action)
	{
		aActions.remove(action);
	}

	public Turn delActions(final Turn turn)
	{
		for (final Action a : turn.getActions()) {
			delAction(a);
		}
		return this;
	}

	public List<Action> getActions()
	{
		return aActions;
	}

	public Turn getActionsOfType(final Class<?>... classTypes)
	{
		final List<Action> actions = new ArrayList<Action>(aActions.size() / 2 + 1);
		for (final Action act : aActions) {
			for (final Class<?> c : classTypes) {
				if (c.isInstance(act)) {
					actions.add(act);
				}
			}
		}
		return new Turn().addActions(actions);
	}

	@Override
	public Iterator<Action> iterator()
	{
		return aActions.iterator();
	}

	public void reEnqueueAction(final Action action)
	{
		delAction(action);
		addAction(action);
	}

	public void reEnqueueAllActions(final List<Action> list)
	{
		for (final Action a : list) {
			reEnqueueAction(a);
		}
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setListAttribute("turns", aActions);
		return j;
	}

	@Override
	public String toString()
	{
		String s = "";
		final String sep = "\n~ THEN ~\n";
		for (final Action a : aActions) {
			s += a + sep;
		}
		return s.substring(0, Math.max(0, s.length() - sep.length()));
	}
}

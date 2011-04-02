package com.evervoid.state.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bushe.swing.event.Logger;
import org.bushe.swing.event.Logger.Level;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVGameState;

/**
 * Represents a game turn. Holds a list of actions.
 */
public class Turn implements Jsonable, Iterable<Action>
{
	private static final Logger sLogger = Logger.getLogger(Turn.class.getName());

	public static Action deserializeAction(final EVGameState state, final Json j) throws Exception
	{
		final String type = j.getStringAttribute("actiontype");
		final Class<?> cl;
		final java.lang.reflect.Constructor<?> co;
		cl = Class.forName(type);
		co = cl.getConstructor(Json.class, EVGameState.class);
		return (Action) co.newInstance(j, state);
	}

	/**
	 * The list of actions played during this turn
	 */
	private final List<Action> aActions = new ArrayList<Action>();

	public Turn()
	{
		// nothing
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
					sLogger.log(Level.WARN, "Got an illegal action serialization while constructing turn");
				}
				else {
					sLogger.log(Level.ERROR,
							"Trying to instantiate an Action that does not exists. Maybe Server version out of sync with Client?");
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

	public List<Action> getActions()
	{
		return aActions;
	}

	public List<Action> getActionsOfType(final String... types)
	{
		final List<Action> actions = new ArrayList<Action>(aActions.size() / 2 + 1);
		for (final Action act : aActions) {
			for (final String type : types) {
				if (type.equals(act.getActionType())) {
					actions.add(act);
				}
			}
		}
		return actions;
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

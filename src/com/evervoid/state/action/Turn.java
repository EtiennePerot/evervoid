package com.evervoid.state.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVGameState;

/**
 * Represents a game turn. Holds a list of actions.
 */
public class Turn implements Jsonable
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
			final String type = action.getStringAttribute("actiontype");
			final Class<?> cl;
			final java.lang.reflect.Constructor<?> co;
			try {
				cl = Class.forName(type);
				co = cl.getConstructor(Json.class, EVGameState.class);
				final Action a = (Action) co.newInstance(action, state);
				aActions.add(a);
			}
			catch (final ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (final SecurityException e) {
				e.printStackTrace();
			}
			catch (final NoSuchMethodException e) {
				e.printStackTrace();
			}
			catch (final IllegalArgumentException e) {
				e.printStackTrace();
			}
			catch (final InstantiationException e) {
				e.printStackTrace();
			}
			catch (final IllegalAccessException e) {
				e.printStackTrace();
			}
			catch (final InvocationTargetException e) {
				e.printStackTrace();
			}
			// TODO - catch Illegal EVException
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

	public void addAllActions(final List<Action> list)
	{
		for (final Action a : list) {
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

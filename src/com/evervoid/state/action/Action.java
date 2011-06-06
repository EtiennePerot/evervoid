package com.evervoid.state.action;

import java.lang.reflect.InvocationTargetException;

import com.evervoid.json.BadJsonInitialization;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVGameState;
import com.evervoid.state.player.Player;

public abstract class Action implements Jsonable
{
	/**
	 * Creates an Action from the Json object passed. The particular type of Action created is determined by the actiontype
	 * attribute. This attribute must be a string in the form of a Class name (ex. com.evervoid.state.action.ship.moveship) and
	 * the given object must have a constructor that take the parameter pair (Json, EVGameState).
	 * 
	 * @param state
	 *            The state object on which the Action will execute.
	 * @param json
	 *            The Json containing the information necessary to create the given Action.
	 * @return The Action object as determined by the contents of the Json.
	 * @throws ClassNotFoundException
	 *             If the class specified in the Json does not exist.
	 * @throws NoSuchMethodException
	 *             If the class specified in the Json does not conform to the (Json, EVGameState) constructor.
	 * @throws SecurityException
	 *             If the (Json, EVGameState) Constructor is a secure field.
	 * @throws InvocationTargetException
	 *             If the (Json, EVGameState) throws an exception.
	 * @throws IllegalAccessException
	 *             If the (Json, EVGameState) constructor is not a visible field.
	 * @throws InstantiationException
	 *             If the class specified by the Json cannot be instantiated (ie. is an abstract class).
	 * @throws IllegalArgumentException
	 *             If the state and json arguments are not instance of EVGameState and Json respectively. (This should never be
	 *             thrown)
	 */
	public static Action deserializeAction(final EVGameState state, final Json json) throws ClassNotFoundException,
			SecurityException, IllegalArgumentException, InstantiationException, InvocationTargetException,
			IllegalAccessException, NoSuchMethodException, BadJsonInitialization
	{
		final String type = json.getStringAttribute("actiontype");
		final Class<?> cl;
		final java.lang.reflect.Constructor<?> co;
		cl = Class.forName(type);
		co = cl.getConstructor(Json.class, EVGameState.class);
		return (Action) co.newInstance(json, state);
	}

	private final Player aPlayer;
	private final EVGameState aState;

	/**
	 * Deserializer constructor.
	 * 
	 * @param j
	 *            The Json containing the Action.
	 * @param state
	 *            The state on which the action will be executed.
	 * @throws IllegalEVActionException
	 *             thrown by children if bad construction is detected.
	 */
	public Action(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		this(state.getPlayerByName(j.getStringAttribute("player")), state);
	}

	/**
	 * Binds an action to a state and a player.
	 * 
	 * @param player
	 *            The player to which the Action belongs.
	 * @param state
	 *            The state on which the Action will be execute.
	 * @throws IllegalEVActionException
	 *             thrown by children if bad construction is detected.
	 */
	public Action(final Player player, final EVGameState state) throws IllegalEVActionException
	{
		aState = state;
		aPlayer = player;
	}

	@Override
	public Action clone()
	{
		try {
			return deserializeAction(aState, toJson());
		}
		catch (final Exception e) {
			// Shouldn't happen if this action is valid in the first place.
			return null;
		}
	}

	/**
	 * Compares two actions
	 * 
	 * @param action
	 *            Action to compare to
	 * @return True if both actions are the same
	 */
	public boolean equals(final Action action)
	{
		if (action == null) {
			return false;
		}
		return toJson().equals(action.toJson());
	}

	/**
	 * Executes the action on the stored state only if the action is valid.
	 * 
	 * @return Whether the action was executed.
	 */
	public boolean execute()
	{
		if (isValid()) {
			executeAction();
			return true;
		}
		return false;
	}

	/**
	 * Called by execute, should be overwritten by children to define execution.
	 */
	protected abstract void executeAction();

	/**
	 * @return The class name of the Action.
	 */
	public String getActionType()
	{
		// returns the class name. NOTE this is used in deserialization and should not change.
		return getClass().getName();
	}

	/**
	 * A short description of the action, display friendly for user.
	 * 
	 * @return The action description.
	 */
	public abstract String getDescription();

	/**
	 * @return The player that owns this Action.
	 */
	public Player getSender()
	{
		return aPlayer;
	}

	public EVGameState getState()
	{
		return aState;
	}

	/**
	 * Check if this Action is valid. Calls the template method isValidAction iff action player is valid in the first place.
	 * Subclasses should only override isValidAction, hence the "final" keyword on this method.
	 */
	public final boolean isValid()
	{
		return aPlayer != null && aState.getPlayerByName(aPlayer.getName()) != null && isValidAction();
	}

	/**
	 * Called by isValid, should be overwritten by children classes to determine validity of action.
	 * 
	 * @return Whether the action is valid to execute.
	 */
	protected abstract boolean isValidAction();

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setAttribute("player", aPlayer.getName());
		j.setAttribute("actiontype", getActionType());
		return j;
	}

	@Override
	public String toString()
	{
		return toJson().toString();
	}
}

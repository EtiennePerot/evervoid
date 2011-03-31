package com.evervoid.state.action;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVGameState;
import com.evervoid.state.player.Player;

public abstract class Action implements Jsonable
{
	protected final Player aPlayer;
	protected final EVGameState aState;

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
		j.setStringAttribute("player", aPlayer.getName());
		j.setStringAttribute("actiontype", getActionType());
		return j;
	}

	@Override
	public String toString()
	{
		return toJson().toString();
	}
}

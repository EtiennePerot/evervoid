package com.evervoid.state.action;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVGameState;
import com.evervoid.state.player.Player;

public abstract class Action implements Jsonable
{
	protected final Player aPlayer;
	protected final EVGameState aState;

	public Action(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		this(state.getPlayerByName(j.getStringAttribute("player")), state);
	}

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

	public boolean execute()
	{
		if (isValid()) {
			executeAction();
			return true;
		}
		return false;
	}

	protected abstract void executeAction();

	public String getActionType()
	{
		return getClass().getName();
	}

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

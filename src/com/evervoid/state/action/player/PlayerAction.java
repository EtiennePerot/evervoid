package com.evervoid.state.action.player;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Action;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.player.Player;

/**
 * Represents an action executed by a Player. Note: All actions have a Player (the creator of the Action); however, this does
 * not always correspond to the target of the Action, hence this class
 */
public abstract class PlayerAction extends Action
{
	/**
	 * The Player being targeted by the PlayerAction.
	 */
	private final Player aTargetPlayer;

	/**
	 * Json Deserializer, the Json must meet the PlayerAction Json Protocol.
	 */
	public PlayerAction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aTargetPlayer = getState().getPlayerByName(j.getStringAttribute("targetPlayer"));
	}

	/**
	 * @param creator
	 *            The originator of the action.
	 * @param target
	 *            The Player this Action targets.
	 * @param state
	 *            The state on which this Action will be executed.
	 * @throws IllegalEVActionException
	 *             If the PlayerAction is malformed.
	 */
	public PlayerAction(final Player creator, final Player target, final EVGameState state) throws IllegalEVActionException
	{
		super(creator, state);
		aTargetPlayer = target;
	}

	/**
	 * @return The Player on which this Action will be carried out.
	 */
	public Player getTargetPlayer()
	{
		return aTargetPlayer;
	}

	/**
	 * Check if this PlayerAction is valid. Calls the template method isValidPlayerAction iff target player is valid in the
	 * first place. Subclasses wishing to detremined when their instance is valid should override isValidPlayerAction.
	 */
	@Override
	protected final boolean isValidAction()
	{
		return aTargetPlayer != null && getState().getPlayerByName(aTargetPlayer.getName()) != null && isValidPlayerAction();
	}

	/**
	 * @return Whether the PlayerAction is valid and legal to be execute on its state.
	 */
	protected abstract boolean isValidPlayerAction();

	@Override
	public Json toJson()
	{
		return super.toJson().setAttribute("targetPlayer", aTargetPlayer.getName());
	}
}

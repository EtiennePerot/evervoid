package com.evervoid.state.action;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Prop;

/**
 * PropAction is a sublcass of {@link Action} and represents an action that a {@link Prop} could take. It is itself an abstract
 * class, and is simply to group functionality common to all Prop related Actions. It implements Action's isValidAction(), but
 * creates isValidPropAction(); this is the method that subclasses should override.
 */
public abstract class PropAction extends Action
{
	private final Prop aProp;

	/**
	 * Creates a PropAction from the contents of the Json and attaches it to the prop associated with the propID determined in
	 * the Json.
	 * 
	 * @param j
	 *            The serialized PropAction
	 * @param state
	 *            The State to which this PropAction will be attached.
	 * @throws IllegalEVActionException
	 *             If the Prop determined by the Json does not exist in the state.
	 */
	public PropAction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		final Prop p = state.getPropFromID(j.getIntAttribute("propID"));
		if (p == null) {
			throw new IllegalEVActionException("Invalid Prop ID");
		}
		aProp = p;
	}

	/**
	 * Sets the aPlayer, aProp and aState variables.
	 * 
	 * @param player
	 *            The Player to which this PropAction belongs.
	 * @param prop
	 *            The Prop carrying out this PropAction.
	 * @param state
	 *            The state on which this PropAction will be executed.
	 * @throws IllegalEVActionException
	 *             If the Action is malformed, this should never happen.
	 */
	public PropAction(final Player player, final Prop prop, final EVGameState state) throws IllegalEVActionException
	{
		super(player, state);
		aProp = prop;
	}

	/**
	 * @return The Prop to carry out this PropAction.
	 */
	protected Prop getProp()
	{
		return aProp;
	}

	/**
	 * Check if this PropAction is valid. Calls the template method isValidPropAction iff prop is valid in the first place.
	 * Subclasses should only override isValidPropAction, hence the "final" keyword on this method.
	 */
	@Override
	protected final boolean isValidAction()
	{
		// Look up again to see if Prop has been destroyed
		return getState().getPropFromID(aProp.getID()) != null && isValidPropAction();
	}

	/**
	 * Abstract function to be overwritten by the sublcasses of PropAction. Called when this Object has already been determined
	 * to be a legal Action. This function should return whether this PropAction instance is a valid PropAction.
	 * 
	 * @return Whether the preconditions of this PropAction are met by it state, making it a valid PropAction.
	 */
	protected abstract boolean isValidPropAction();

	@Override
	public Json toJson()
	{
		return super.toJson().setAttribute("propID", aProp.getID());
	}
}

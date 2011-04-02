package com.evervoid.state.action;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Prop;

public abstract class PropAction extends Action
{
	private final Prop aProp;

	public PropAction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		final Prop p = state.getPropFromID(j.getIntAttribute("propID"));
		if (p == null) {
			throw new IllegalEVActionException("Invalid Prop ID");
		}
		aProp = p;
	}

	public PropAction(final Player player, final Prop prop, final EVGameState state) throws IllegalEVActionException
	{
		super(player, state);
		aProp = prop;
	}

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

	protected abstract boolean isValidPropAction();

	@Override
	public Json toJson()
	{
		return super.toJson().setIntAttribute("propID", aProp.getID());
	}
}

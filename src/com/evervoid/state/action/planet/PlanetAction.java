package com.evervoid.state.action.planet;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.action.PropAction;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Ship;

public abstract class PlanetAction extends PropAction
{
	public PlanetAction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		if (!(getProp() instanceof Planet)) {
			throw new IllegalEVActionException("Prop is not a planet");
		}
	}

	public PlanetAction(final Player player, final String actionType, final Planet planet, final EVGameState state)
			throws IllegalEVActionException
	{
		super(player, actionType, planet, state);
	}

	protected Planet getPlanet()
	{
		return (Planet) getProp();
	}

	protected abstract boolean isValidPlanetAction();

	/**
	 * Check if this PlanetAction is valid. Calls the template method isValidPlanetAction iff planet is valid in the first
	 * place. Subclasses should only override isValidPlanetAction, hence the "final" keyword on this method.
	 */
	@Override
	protected final boolean isValidPropAction()
	{
		return getProp() instanceof Ship && isValidPlanetAction();
	}
}

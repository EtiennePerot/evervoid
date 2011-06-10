package com.evervoid.state.action.planet;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.action.PropAction;
import com.evervoid.state.prop.Planet;

/**
 * PlanetAction wraps common functionality of PropActions carried out on Planets.
 */
public abstract class PlanetAction extends PropAction
{
	/**
	 * Json Deserializer for PlanetAction; the Json must meet the PlanetAction Json Protocol.
	 * 
	 * @throws IllegalEVActionException
	 *             If the Json does not meet its protocol, or if the PlanetAction is malformed.
	 */
	public PlanetAction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		if (!(getProp() instanceof Planet)) {
			throw new IllegalEVActionException("Prop is not a planet");
		}
	}

	/**
	 * Creates a new PlanetAction, determining all the Action parameters based on the Planet's state.
	 * 
	 * @param planet
	 *            The Planet on which the action will be executed.
	 * @throws IllegalEVActionException
	 *             If the Action is malformed.
	 */
	public PlanetAction(final Planet planet) throws IllegalEVActionException
	{
		super(planet.getPlayer(), planet, planet.getState());
	}

	/**
	 * @return The Planet on which this PlanetAction is being executed.
	 */
	public Planet getPlanet()
	{
		return (Planet) getProp();
	}

	/**
	 * @return Whether this instance is a valid PlanetAction.
	 */
	protected abstract boolean isValidPlanetAction();

	/**
	 * Checks if this PlanetAction is valid. Calls the template method isValidPlanetAction iff planet is valid in the first
	 * place. Subclasses wishing to determine whether their instance is a valid PlanetAction should override
	 * isValidPlanetAction.
	 */
	@Override
	protected final boolean isValidPropAction()
	{
		return getProp() instanceof Planet && isValidPlanetAction();
	}
}

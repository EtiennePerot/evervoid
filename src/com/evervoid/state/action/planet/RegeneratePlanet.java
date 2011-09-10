package com.evervoid.state.action.planet;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Planet;

/**
 * RegeneratePlanet is a PlanetAction that modifies the Planet's current health or shields.
 */
public class RegeneratePlanet extends PlanetAction
{
	/**
	 * The amount by which this action will change the Planet's health.
	 */
	private final int aHealthAmount;
	/**
	 * The amount by which this action will change the Planet's shields.
	 */
	private final int aShieldsAmount;

	/**
	 * Json deserializer; the Json must conform to the RegeneratePlanet Json Protocol.
	 * 
	 * @param j
	 *            The Json to deserialize.
	 * @param state
	 *            The state on which to execute this action.
	 * @throws IllegalEVActionException
	 *             If the Json does not meet the protocol, or if the Action is malformed.
	 */
	public RegeneratePlanet(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aHealthAmount = j.getIntAttribute("health");
		aShieldsAmount = j.getIntAttribute("shields");
	}

	/**
	 * Creates a RegeneratePlanet action using the Planet's current health and shield regeneration rates.
	 * 
	 * @param planet
	 *            The Planet to regenerate.
	 * @throws IllegalEVActionException
	 *             If the action is malformed.
	 */
	public RegeneratePlanet(final Planet planet) throws IllegalEVActionException
	{
		super(planet);
		aHealthAmount = planet.getHealthRegenRate();
		aShieldsAmount = planet.getShieldRegenRate();
	}

	@Override
	protected void executeAction()
	{
		getPlanet().addHealth(aHealthAmount);
		getPlanet().addShields(aShieldsAmount);
	}

	@Override
	public String getDescription()
	{
		return "regenerating " + getPlanet().getPlanetType() + " resources";
	}

	@Override
	protected boolean isValidPlanetAction()
	{
		// add health caps accordingly, not sure what needs to be checked here
		return true;
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("health", aHealthAmount);
		j.setAttribute("shields", aShieldsAmount);
		return j;
	}
}

package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Ship;

/**
 * Regenerates a Ship by it's current health, shield, and radiation regeneration rates.
 */
public class RegenerateShip extends ShipAction
{
	/**
	 * The amount of health being regenerated.
	 */
	public final int aHealthAmount;
	/**
	 * The amount of radiation being regenerated.
	 */
	public final int aRadiationAmount;
	/**
	 * The amount of shields being regenerated.
	 */
	public final int aShieldAmount;

	/**
	 * Json deserializer.
	 * 
	 * @param j
	 *            The Json serialization of the action.
	 * @param state
	 *            The state on which this action will be executed.
	 * @throws IllegalEVActionException
	 *             If the action is not legal.
	 */
	public RegenerateShip(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aHealthAmount = j.getIntAttribute("health");
		aShieldAmount = j.getIntAttribute("shields");
		aRadiationAmount = j.getIntAttribute("radiation");
	}

	/**
	 * A new RegenerateShip action.
	 * 
	 * @param ship
	 *            The ship being regenerated.
	 * @throws IllegalEVActionException
	 *             If the action is not valid.
	 */
	public RegenerateShip(final Ship ship) throws IllegalEVActionException
	{
		super(ship);
		aHealthAmount = ship.getHealthRegenRate();
		aShieldAmount = ship.getShieldRegenRate();
		aRadiationAmount = ship.getRadiationRate();
	}

	@Override
	public void executeAction()
	{
		getShip().addHealth(aHealthAmount);
		getShip().addShields(aShieldAmount);
		getShip().addRadiation(aRadiationAmount);
	}

	@Override
	public String getDescription()
	{
		return "regeneration ship supplies";
	}

	@Override
	protected boolean isValidShipAction()
	{
		// If the amount is too big, the ship will cap it.
		// nothing to do here atm
		return true;
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("health", aHealthAmount);
		j.setAttribute("shields", aShieldAmount);
		j.setAttribute("radiation", aRadiationAmount);
		return j;
	}
}

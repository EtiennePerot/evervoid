package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Ship;

public class RegenerateShip extends ShipAction
{
	public final int aHealthAmount;
	public final int aRadiationAmount;
	public final int aShieldAmount;

	public RegenerateShip(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aHealthAmount = j.getIntAttribute("health");
		aShieldAmount = j.getIntAttribute("shields");
		aRadiationAmount = j.getIntAttribute("radiation");
	}

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
		j.setIntAttribute("health", aHealthAmount);
		j.setIntAttribute("shields", aShieldAmount);
		j.setIntAttribute("radiation", aRadiationAmount);
		return j;
	}
}

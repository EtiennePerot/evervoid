package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Ship;

public class ModifyHealth extends ShipAction
{
	public final int aHealthAmount;

	public ModifyHealth(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aHealthAmount = j.getIntAttribute("offset");
	}

	public ModifyHealth(final Ship ship, final EVGameState state, final int offset) throws IllegalEVActionException
	{
		super(ship, state);
		aHealthAmount = offset;
	}

	@Override
	public void executeAction()
	{
		getShip().addHealth(aHealthAmount);
	}

	@Override
	protected boolean isValidShipAction()
	{
		// don't see why this action should ever be invalid.
		// if the amount is too big, the ship'll cap it.
		return true;
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("offset", aHealthAmount);
		return j;
	}
}

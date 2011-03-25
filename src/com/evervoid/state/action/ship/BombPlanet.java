package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;

public class BombPlanet extends ShipAction
{
	public BombPlanet(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute()
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean isValidShipAction()
	{
		// TODO Auto-generated method stub
		return false;
	}
}

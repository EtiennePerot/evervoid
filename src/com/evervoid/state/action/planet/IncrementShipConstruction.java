package com.evervoid.state.action.planet;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Planet;

public class IncrementShipConstruction extends PlanetAction
{
	public IncrementShipConstruction(final EVGameState state, final Planet planet) throws IllegalEVActionException
	{
		super(planet.getPlayer(), "IncrementShipConstruction", planet, state);
	}

	public IncrementShipConstruction(final Json j, final EVGameState state) throws IllegalEVActionException
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
	protected boolean isValidPlanetAction()
	{
		// TODO Auto-generated method stub
		return false;
	}
}

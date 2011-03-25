package com.evervoid.state.action.building;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.action.planet.PlanetAction;

public class ConstructBuilding extends PlanetAction
{
	public ConstructBuilding(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void executeAction()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isValidPlanetAction()
	{
		// TODO Auto-generated method stub
		return false;
	}
}

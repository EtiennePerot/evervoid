package com.evervoid.state.action.building;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.action.planet.PlanetAction;
import com.evervoid.state.building.Building;

public class ConstructBuilding extends PlanetAction
{
	private final Building aNewBuilding;

	public ConstructBuilding(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aNewBuilding = new Building(j.getAttribute("newBuilding"), aState);
	}

	@Override
	protected void executeAction()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public String getDescription()
	{
		return " is building a " + aNewBuilding.getType();
	}

	@Override
	public boolean isValidPlanetAction()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("newBuilding", aNewBuilding);
		return j;
	}
}

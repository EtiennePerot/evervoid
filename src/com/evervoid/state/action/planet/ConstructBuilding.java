package com.evervoid.state.action.planet;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Planet;

public class ConstructBuilding extends PlanetAction
{
	private final String aBuildingType;

	public ConstructBuilding(final EVGameState state, final Planet planet, final String buildingType)
			throws IllegalEVActionException
	{
		super(planet.getPlayer(), planet, state);
		aBuildingType = buildingType;
	}

	public ConstructBuilding(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aBuildingType = j.getStringAttribute("buildingType");
	}

	@Override
	protected void executeAction()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public String getDescription()
	{
		return " is building a " + aBuildingType;
	}

	@Override
	public boolean isValidPlanetAction()
	{
		// TODO - check that there is room on the planet
		return true;
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setStringAttribute("buildingType", aBuildingType);
		return j;
	}
}

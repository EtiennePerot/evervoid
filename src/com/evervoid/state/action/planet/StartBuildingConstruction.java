package com.evervoid.state.action.planet;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.building.Building;
import com.evervoid.state.data.BuildingData;

public class StartBuildingConstruction extends PlanetAction
{
	final BuildingData aBuildingData;
	final int aBuildingSlot;

	public StartBuildingConstruction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aBuildingData = state.getBuildingData(getSender().getRaceData().getType(), j.getStringAttribute("type"));
		aBuildingSlot = j.getIntAttribute("buildingSlot");
	}

	@Override
	protected void executeAction()
	{
		getSender().removeResources(aBuildingData.getCost());
		final Building b = new Building(getState(), getPlanet(), aBuildingData, false);
		getPlanet().addBuilding(aBuildingSlot, b);
	}

	@Override
	public String getDescription()
	{
		return "starting construction on a " + aBuildingData.getTitle();
	}

	@Override
	protected boolean isValidPlanetAction()
	{
		return getPlanet().hasSlot(aBuildingSlot) && getPlanet().getBuildingAt(aBuildingSlot) == null
				&& getSender().hasResources(aBuildingData.getCost());
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setStringAttribute("type", aBuildingData.getType());
		j.setIntAttribute("buildingSlot", aBuildingSlot);
		return j;
	}
}

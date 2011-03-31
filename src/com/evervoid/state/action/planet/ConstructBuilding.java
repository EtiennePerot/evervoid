package com.evervoid.state.action.planet;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Planet;

public class ConstructBuilding extends PlanetAction
{
	private final String aBuildingType;
	private final int aTargetSlot;

	public ConstructBuilding(final EVGameState state, final Planet planet, final int slot, final String buildingType)
			throws IllegalEVActionException
	{
		super(planet.getPlayer(), planet, state);
		aTargetSlot = slot;
		aBuildingType = buildingType;
	}

	public ConstructBuilding(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aBuildingType = j.getStringAttribute("buildingType");
		aTargetSlot = j.getIntAttribute("slot");
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
		// TODO: Check player resources
		return getPlanet().isSlotFree(aTargetSlot);
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setStringAttribute("buildingType", aBuildingType);
		j.setIntAttribute("slot", aTargetSlot);
		return j;
	}
}

package com.evervoid.state.action.planet;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;
import com.evervoid.state.prop.Planet;

public class IncrementBuildingConstruction extends PlanetAction
{
	private final BuildingData aBuildingData;
	private final int aTargetSlot;

	public IncrementBuildingConstruction(final EVGameState state, final Planet planet, final int slot, final String buildingType)
			throws IllegalEVActionException
	{
		super(planet.getPlayer(), planet, state);
		aTargetSlot = slot;
		aBuildingData = getState().getBuildingData(planet.getPlayer().getRaceData().getType(), buildingType);
		if (aBuildingData == null) {
			throw new IllegalEVActionException("Invalid building type.");
		}
	}

	public IncrementBuildingConstruction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aBuildingData = getState().getBuildingData(getPlanet().getPlayer().getRaceData().getType(),
				j.getStringAttribute("buildingtype"));
		aTargetSlot = j.getIntAttribute("slot");
	}

	@Override
	public IncrementBuildingConstruction clone()
	{
		try {
			return new IncrementBuildingConstruction(getState(), getPlanet(), aTargetSlot, aBuildingData.getType());
		}
		catch (final IllegalEVActionException e) {
			// Not going to happen if this action is valid in the first place
			return null;
		}
	}

	@Override
	protected void executeAction()
	{
		getPlanet().getPlayer().subtractResources(getPartialCost());
		getPlanet().incrementBuilding(aTargetSlot, aBuildingData);
	}

	public BuildingData getBuildingData()
	{
		return aBuildingData;
	}

	@Override
	public String getDescription()
	{
		return " is building a " + aBuildingData.getTitle();
	}

	public ResourceAmount getPartialCost()
	{
		return aBuildingData.getCost().divide(aBuildingData.getBuildTime());
	}

	@Override
	public boolean isValidPlanetAction()
	{
		final Player planetOwner = getPlanet().getPlayer();
		// Check planet ownership
		if (!getSender().equals(planetOwner)) {
			return false;
		}
		// Check resources
		if (!planetOwner.hasResources(getPartialCost())) {
			return false;
		}
		return !getPlanet().isBuildingComplete(aTargetSlot);
	}

	/**
	 * @return Whether the construction is now over for the building or not
	 */
	public boolean shouldContinueBuilding()
	{
		return !getPlanet().isBuildingComplete(aTargetSlot);
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setStringAttribute("buildingtype", aBuildingData.getType());
		j.setIntAttribute("slot", aTargetSlot);
		return j;
	}
}

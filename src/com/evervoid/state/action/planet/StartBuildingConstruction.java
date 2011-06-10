package com.evervoid.state.action.planet;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.building.Building;
import com.evervoid.state.data.BuildingData;

/**
 * StartBuildingConstruction deals with everything around starting construction on a Building, including making the owner pay
 * the cost.
 */
public class StartBuildingConstruction extends PlanetAction
{
	/**
	 * The type of Building this action will create.
	 */
	final BuildingData aBuildingData;
	/**
	 * The slot on the planet this Building will occupy.
	 */
	final int aBuildingSlot;

	/**
	 * Json deserializer; the Json must conform to the StartBuildingConstruction Json Protocol.
	 * 
	 * @throws IllegalEVActionException
	 *             If the Json does not meet the Protocol, or the action is malformed.
	 */
	public StartBuildingConstruction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aBuildingData = state.getBuildingData(getSender().getRaceData().getType(), j.getStringAttribute("type"));
		aBuildingSlot = j.getIntAttribute("buildingSlot");
	}

	@Override
	protected void executeAction()
	{
		getSender().subtractResources(aBuildingData.getCost());
		final Building b = new Building(getState(), getPlanet(), aBuildingData, false);
		getPlanet().addBuilding(aBuildingSlot, b);
	}

	@Override
	public String getDescription()
	{
		return "Starting construction on a " + aBuildingData.getTitle();
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
		j.setAttribute("type", aBuildingData.getType());
		j.setAttribute("buildingSlot", aBuildingSlot);
		return j;
	}
}

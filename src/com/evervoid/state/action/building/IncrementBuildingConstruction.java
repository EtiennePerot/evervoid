package com.evervoid.state.action.building;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.action.planet.PlanetAction;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;
import com.evervoid.state.prop.Planet;

/**
 * IncrementBuildingConstruction adds a turn of progress to the Building currently being constructed on the local Planet.
 */
public class IncrementBuildingConstruction extends PlanetAction
{
	/**
	 * The type of Building being constructed.
	 */
	private final BuildingData aBuildingData;
	/**
	 * The slot in which this Building is being constructed.
	 */
	private final int aTargetSlot;

	/**
	 * Creates a new IncrementShipConstruction using the given parameters.
	 * 
	 * @param state
	 *            The state on which this action will be executed.
	 * @param planet
	 *            The Planet on which the construction is happening.
	 * @param slot
	 *            The slot on which to construct this Building.
	 * @param buildingType
	 *            The type of Building being constructed.
	 * @throws IllegalEVActionException
	 *             If the action is malformed.
	 */
	public IncrementBuildingConstruction(final EVGameState state, final Planet planet, final int slot, final String buildingType)
			throws IllegalEVActionException
	{
		super(planet);
		aTargetSlot = slot;
		aBuildingData = getState().getBuildingData(planet.getPlayer().getRaceData().getType(), buildingType);
		if (aBuildingData == null) {
			throw new IllegalEVActionException("Invalid building type.");
		}
	}

	/**
	 * Json deserializer; the Json must conform to the IncrementBuildingConstruction Json Protocol.
	 * 
	 * @throws IllegalEVActionException
	 *             If the Json does not meet the protocol, or if the action is malformed.
	 */
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

	/**
	 * @return The type of Building being constructed.
	 */
	public BuildingData getBuildingData()
	{
		return aBuildingData;
	}

	@Override
	public String getDescription()
	{
		return "Building: " + aBuildingData.getTitle();
	}

	/**
	 * @return The cost of constructing this fraction of the Building.
	 */
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
		// Additional building logic is handled in Planet
		return true;
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
		j.setAttribute("buildingtype", aBuildingData.getType());
		j.setAttribute("slot", aTargetSlot);
		return j;
	}
}

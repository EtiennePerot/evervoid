package com.evervoid.state.action.building;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.building.Building;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;
import com.evervoid.state.prop.Ship;
import com.evervoid.utils.MathUtils;

public class IncrementShipConstruction extends BuildingAction
{
	/**
	 * ShipData of the ship about to be built
	 */
	private final ShipData aShipData;
	/**
	 * Computed server-side to determine where the ship will appear.
	 */
	private GridLocation aShipTargetLocation = null;

	public IncrementShipConstruction(final EVGameState state, final Building building, final ShipData shipData)
			throws IllegalEVActionException
	{
		super(state, building);
		aShipData = shipData;
	}

	public IncrementShipConstruction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aShipData = getBuilding().getPlayer().getRaceData().getShipData(j.getStringAttribute("shiptype"));
		if (j.hasAttribute("targetlocation")) {
			aShipTargetLocation = new GridLocation(j.getAttribute("targetlocation"));
		}
	}

	@Override
	protected void executeAction()
	{
		getBuilding().getPlayer().subtractResources(getPartialCost());
		if (getBuilding().incrementShipProgress(aShipData)) {
			final SolarSystem ss = (SolarSystem) getBuilding().getPlanet().getContainer();
			if (aShipTargetLocation == null) { // aShipTargetLocation will only be null server-side
				aShipTargetLocation = (GridLocation) MathUtils.getRandomElement(ss.getNeighbours(getPlanet(),
						aShipData.getDimension()));
			}
			if (aShipTargetLocation != null) {
				final Ship newShip = new Ship(getState().getNextPropID(), getBuilding().getPlayer(), ss, aShipTargetLocation,
						aShipData.getType(), getState());
				getState().registerProp(newShip, ss);
				getBuilding().resetShipConstruction();
			}
			else {
				// Can't find a spot for the new ship! Do nothing, the client will resend the action next turn and this will be
				// checked again.
			}
		}
	}

	@Override
	public String getDescription()
	{
		return "Building ship: " + aShipData.getTitle();
	}

	private ResourceAmount getPartialCost()
	{
		return aShipData.getBaseCost().divide(aShipData.getBaseBuildTime());
	}

	public ShipData getShipData()
	{
		return aShipData;
	}

	@Override
	protected boolean isValidBuildingAction()
	{
		// Check if building is fully erect
		if (!getBuilding().isBuildingComplete()) {
			return false;
		}
		final Player buildingOwner = getBuilding().getPlayer();
		// Check planet ownership
		if (!getSender().equals(buildingOwner)) {
			return false;
		}
		// Check resources
		if (!buildingOwner.hasResources(getPartialCost())) {
			return false;
		}
		return getBuilding().isBuildingComplete();
	}

	public boolean shouldContinueBuilding()
	{
		return getBuilding().isBuildingShip();
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setStringAttribute("shiptype", aShipData.getType());
		if (aShipTargetLocation != null) {
			j.setAttribute("targetlocation", aShipTargetLocation);
		}
		return j;
	}
}

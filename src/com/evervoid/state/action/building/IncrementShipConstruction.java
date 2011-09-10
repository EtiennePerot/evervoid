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

/**
 * IncrementShipConstruction continues construction on a Ship.
 */
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

	/**
	 * @param building
	 *            The Building on which the construction is happening.
	 * @param shipData
	 *            The Ship type being constructed.
	 * @throws IllegalEVActionException
	 *             If the action is malformed.
	 */
	public IncrementShipConstruction(final Building building, final ShipData shipData) throws IllegalEVActionException
	{
		super(building);
		aShipData = shipData;
	}

	/**
	 * Json deserializer; the Json must conform to the IncrementShipConstruction Json Protocol.
	 * 
	 * @param j
	 *            The Json to deserialize.
	 * @param state
	 *            The state on which to execute this action.
	 * @throws IllegalEVActionException
	 *             If the Json does not meet the protocol, or if the action is malformed.
	 */
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
				aShipTargetLocation = MathUtils.getRandomElement(ss.getFreeNeighbours(getPlanet(), aShipData.getDimension()));
			}
			if (aShipTargetLocation != null) {
				final Ship newShip = new Ship(getBuilding().getPlayer(), ss, aShipTargetLocation, aShipData.getType(),
						getState());
				getState().registerProp(newShip, ss);
				getBuilding().resetShipConstruction();
			}
			else {
				// Can't find a spot for the new ship! Do nothing, the client will resend the action next turn and this will be
				// checked again.
				// Give the player his money back
				getBuilding().getPlayer().addResources(getPartialCost());
			}
		}
	}

	@Override
	public String getDescription()
	{
		return "Building ship: " + aShipData.getTitle();
	}

	/**
	 * @return The fraction of the cost to be paid for this increment.
	 */
	private ResourceAmount getPartialCost()
	{
		return aShipData.getBaseCost().divide(aShipData.getBaseBuildTime());
	}

	/**
	 * @return The type of Ship being built.
	 */
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

	/**
	 * @return True if Ship construction should continue, will be false if the Ship has just finished constructing.
	 */
	public boolean shouldContinueBuilding()
	{
		return getBuilding().isConstructingShip();
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("shiptype", aShipData.getType());
		if (aShipTargetLocation != null) {
			j.setAttribute("targetlocation", aShipTargetLocation);
		}
		return j;
	}
}

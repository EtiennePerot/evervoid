package com.evervoid.state.action.building;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.building.Building;

public class IncrementShipConstruction extends BuildingAction
{
	private final String aShipType;

	public IncrementShipConstruction(final EVGameState state, final Building building, final String shipType)
			throws IllegalEVActionException
	{
		super(state, building);
		aShipType = shipType;
		getSender().getRaceData().getShipData(shipType);
	}

	public IncrementShipConstruction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aShipType = j.getStringAttribute("shipType");
	}

	@Override
	protected void executeAction()
	{
		// all this does is decrease the build count, it is your job to actually build the ship when count == 0
		// the reason it is not done here is that it requires a game state, and this action does not contain one (nor should it)
		if (getBuilding().getShipProgress(aShipType) == -1) {
			// no progress, start building, debit cost
			getBuilding().startBuildingShip(getSender().getRaceData().getShipData(aShipType));
			getSender().removeResources(getSender().getRaceData().getShipData(aShipType).getBaseCost());
		}
		else {
			getBuilding().continueBuildingShip();
		}
	}

	@Override
	public String getDescription()
	{
		return "continuting constrction on " + aShipType;
	}

	@Override
	protected boolean isValidBuildingAction()
	{
		return (getBuilding().getShipProgress(aShipType) == -1)
				|| getSender().hasResources(getSender().getRaceData().getShipData(aShipType).getBaseCost());
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setStringAttribute("shipType", aShipType);
		return j;
	}
}

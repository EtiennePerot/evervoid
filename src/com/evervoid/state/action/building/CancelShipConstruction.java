package com.evervoid.state.action.building;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.building.Building;

public class CancelShipConstruction extends BuildingAction
{
	public CancelShipConstruction(final EVGameState state, final Building building) throws IllegalEVActionException
	{
		super(state, building);
	}

	@Override
	protected void executeAction()
	{
		getBuilding().cancelShipConstruction();
	}

	@Override
	public String getDescription()
	{
		return "Cancelling ship construction";
	}

	@Override
	protected boolean isValidBuildingAction()
	{
		// Owner checking is done above in BuildingAction.
		return getBuilding().isBuildingComplete() && getBuilding().isBuildingShip();
	}

	@Override
	public Json toJson()
	{
		// Doesn't actually need to include any more info than BuildingAction. Just overriding it here anyway for consistency
		// and to show it's not been forgotten.
		return super.toJson();
	}
}

package com.evervoid.state.action.building;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.building.Building;

public class CancelShipConstruction extends BuildingAction
{
	public CancelShipConstruction(final Building building) throws IllegalEVActionException
	{
		super(building);
	}

	public CancelShipConstruction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		// Even though this does nothing, it is NECESSARY to be there for the constructor to be visible for reflection
		super(j, state);
	}

	@Override
	protected void executeAction()
	{
		getBuilding().resetShipConstruction();
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
		return getBuilding().isBuildingComplete() && getBuilding().isConstructingShip();
	}

	@Override
	public Json toJson()
	{
		// Doesn't actually need to include any more info than BuildingAction. Just overriding it here anyway for consistency
		// and to show it's not been forgotten.
		return super.toJson();
	}
}

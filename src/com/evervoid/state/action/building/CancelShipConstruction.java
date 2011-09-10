package com.evervoid.state.action.building;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.building.Building;
import com.evervoid.state.prop.Ship;

/**
 * CancelShipConstruction is an action used to reset all progress of the local Building on a {@link Ship}
 */
public class CancelShipConstruction extends BuildingAction
{
	/**
	 * Constructs a CancelShipConstruction using the Building and its variables.
	 * 
	 * @param building
	 *            The Building to use in constructing this action.
	 * @throws IllegalEVActionException
	 *             If the action is malformed.
	 */
	public CancelShipConstruction(final Building building) throws IllegalEVActionException
	{
		super(building);
	}

	/**
	 * Json deserializer; the Json must conform to the CancelShipConstruction Protocol.
	 * 
	 * @param j
	 *            The Json to deserialize.
	 * @param state
	 *            The state on which to execute this action.
	 * @throws IllegalEVActionException
	 *             If the Json does not meet the protocol, or if the action is malformed.
	 */
	public CancelShipConstruction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		// The point here is to expose the constructor, this needs to be done for the reflective deserialization used
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

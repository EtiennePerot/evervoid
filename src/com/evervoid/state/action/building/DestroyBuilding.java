package com.evervoid.state.action.building;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.building.Building;

/**
 * Destroys a building. Called by the building's owner only. Since buildings may be partially built, this action will also work
 * to cancel in-construction buildings.
 */
public class DestroyBuilding extends BuildingAction
{
	/**
	 * Creates a DestroyBuilding using the variables in the Building parameter.
	 * 
	 * @param building
	 *            The Building from which to pull the necessary variables.
	 * @throws IllegalEVActionException
	 *             If the action is malformed.
	 */
	public DestroyBuilding(final Building building) throws IllegalEVActionException
	{
		super(building);
	}

	/**
	 * Json deserializer; the Json must meet the DestroyBuilding Json Protocol.
	 * 
	 * @throws IllegalEVActionException
	 *             If the Json does not meet its protocol, or if the action is malformed.
	 */
	public DestroyBuilding(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		// The point here is to expose the constructor, this needs to be done for the reflective deserialization used
		super(j, state);
	}

	@Override
	protected void executeAction()
	{
		getBuilding().delete();
	}

	@Override
	public String getDescription()
	{
		return "Destroying " + getBuilding().getData().getTitle();
	}

	@Override
	protected boolean isValidBuildingAction()
	{
		// Always true as long as the building exists, which is true if we got this far.
		// Owner checking is also done above in BuildingAction.
		// Do not check for building completion either, because this action works for partially-built buildings in roder to
		// cancel their construction.
		return true;
	}

	@Override
	public Json toJson()
	{
		// Doesn't actually need to include any more info than BuildingAction. Just overriding it here anyway for consistency
		// and to show it's not been forgotten.
		return super.toJson();
	}
}

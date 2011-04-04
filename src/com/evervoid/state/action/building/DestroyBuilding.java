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
	public DestroyBuilding(final EVGameState state, final Building building) throws IllegalEVActionException
	{
		super(state, building);
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

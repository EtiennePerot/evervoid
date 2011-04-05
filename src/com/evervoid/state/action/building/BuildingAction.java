package com.evervoid.state.action.building;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Action;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.building.Building;
import com.evervoid.state.prop.Planet;

public abstract class BuildingAction extends Action
{
	private final Building aBuilding;

	public BuildingAction(final Building building) throws IllegalEVActionException
	{
		super(building.getPlayer(), building.getState());
		aBuilding = building;
	}

	public BuildingAction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aBuilding = state.getBuildingFromID(j.getIntAttribute("building"));
	}

	public Building getBuilding()
	{
		return aBuilding;
	}

	protected Planet getPlanet()
	{
		return aBuilding.getPlanet();
	}

	@Override
	protected final boolean isValidAction()
	{
		return getPlanet().getPlayer().equals(getSender()) && isValidBuildingAction();
	}

	protected abstract boolean isValidBuildingAction();

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("building", aBuilding.getID());
		return j;
	}
}

package com.evervoid.state.action.building;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Action;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.building.Building;
import com.evervoid.state.prop.Planet;

/**
 * BuildingAction wraps functionality of Actions pertaining to Buildings. It overwrites the isValidAction() and replaces it with
 * isValidBuildingAction(), that is the function that sublasses should implement.
 */
public abstract class BuildingAction extends Action
{
	/**
	 * The Building by which this action will be executed.
	 */
	private final Building aBuilding;

	/**
	 * Creates a BuildingAction, using the building's parameters to fill in all the necessary information.
	 * 
	 * @param building
	 *            The Building from which to get all the local parameters.
	 * @throws IllegalEVActionException
	 *             If the BuildingAction is malformed.
	 */
	public BuildingAction(final Building building) throws IllegalEVActionException
	{
		super(building.getPlayer(), building.getState());
		aBuilding = building;
	}

	/**
	 * Json deserializer; the Json must conform to the BuildingAction Json Protocol.
	 * 
	 * @param j
	 *            The Json to deserialize.
	 * @param state
	 *            The state on which to execute this action.
	 * @throws IllegalEVActionException
	 *             If the Json does not meet its protocol, or if the action is malformed.
	 */
	public BuildingAction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aBuilding = state.getBuildingFromID(j.getIntAttribute("building"));
	}

	/**
	 * @return The Building executing this action.
	 */
	public Building getBuilding()
	{
		return aBuilding;
	}

	/**
	 * @return The Planet on which this Building is located.
	 */
	protected Planet getPlanet()
	{
		return getBuilding().getPlanet();
	}

	@Override
	protected final boolean isValidAction()
	{
		return getPlanet().getPlayer().equals(getSender()) && isValidBuildingAction();
	}

	/**
	 * This is the function that subclasses should override if they wish to determine whether their instance is valid.
	 * 
	 * @return Whether this instance of BuildingAction is valid to be executed.
	 */
	protected abstract boolean isValidBuildingAction();

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("building", aBuilding.getID());
		return j;
	}
}

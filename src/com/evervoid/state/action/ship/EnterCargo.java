package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;

public class EnterCargo extends ShipAction
{
	private final Ship aContainerShip;
	private GridLocation aDestination;
	private final MoveShip aUnderlyingMove;

	public EnterCargo(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aContainerShip = (Ship) state.getPropFromID(j.getIntAttribute("cargoShip"));
		aUnderlyingMove = new MoveShip(j.getAttribute("movement"), state);
	}

	public EnterCargo(final Ship actionShip, final Ship cargoShip, final EVGameState state) throws IllegalEVActionException
	{
		super(actionShip, state);
		aContainerShip = cargoShip;
		final GridLocation closestLocation = getShip().getLocation().getClosestOrigin(
				cargoShip.getNeighborOrigins(getShip().getDimension()));
		aUnderlyingMove = new MoveShip(getShip(), closestLocation.origin, getState());
		if (!aUnderlyingMove.isValid()) {
			throw new IllegalEVActionException("Bad underlying move");
		}
	}

	@Override
	protected void executeAction()
	{
		getShip().enterCargo(aContainerShip, aUnderlyingMove.getFinalPath());
	}

	@Override
	public String getDescription()
	{
		return "Docking in " + aContainerShip.getShipType() + "'s cargo hold";
	}

	public Prop getTarget()
	{
		return aContainerShip;
	}

	public MoveShip getUnderlyingMove()
	{
		return aUnderlyingMove;
	}

	@Override
	protected boolean isValidShipAction()
	{
		// 1. Container has space
		// 2. Container is in a solar system
		// 3. Both ships are in the same solar system
		// 4. Space next to container is open
		// FIXME: Ensure that only noncontainer-ships can enter container ships, to prevent some bad recursion
		return (aContainerShip.canHold(getShip()) && getShip().getContainer() instanceof SolarSystem)
				&& (getShip().getContainer().equals(aContainerShip.getContainer())) && aUnderlyingMove.isValidShipAction();
	}

	public void setDestination(final GridLocation location)
	{
		aDestination = location;
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("cargoShip", aContainerShip.getID());
		j.setAttribute("cargoLocation", aDestination);
		j.setAttribute("movement", aUnderlyingMove);
		return j;
	}
}

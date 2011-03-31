package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Ship;

public class EnterCargo extends ShipAction
{
	private final Ship aContainerShip;
	private final GridLocation aDestination;

	public EnterCargo(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aContainerShip = (Ship) state.getPropFromID(j.getIntAttribute("cargoShip"));
		aDestination = new GridLocation(j.getAttribute("cargoLocation"));
	}

	@Override
	protected void executeAction()
	{
		// TODO - underlying move
		getShip().leaveContainer();
		aContainerShip.addElem(getShip());
	}

	@Override
	protected boolean isValidShipAction()
	{
		// 1. Container has space
		// 2. Container is in a solar system
		// 3. Both ships are in the same solar system
		// 4. Space next to container is open
		return (aContainerShip.canHold(getShip()) && getShip().getContainer() instanceof SolarSystem)
				&& (getShip().getContainer().equals(aContainerShip.getContainer()))
				&& (!((SolarSystem) getShip().getContainer()).isOccupied(aDestination));
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("cargoShip", aContainerShip.getID());
		j.setAttribute("cargoLocation", aDestination);
		return j;
	}
}

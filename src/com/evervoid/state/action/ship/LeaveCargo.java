package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Ship;

public class LeaveCargo extends ShipAction
{
	private final Ship aContainerShip;
	private final GridLocation aDestination;

	public LeaveCargo(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aContainerShip = (Ship) state.getPropFromID(j.getIntAttribute("cargoShip"));
		aDestination = new GridLocation(j.getAttribute("cargoLocation"));
	}

	@Override
	protected void executeAction()
	{
		// remove from cargo
		aContainerShip.removeElem(getShip());
		// set new location
		getShip().move(aDestination, null);
		// add to surrounding solar system
		aContainerShip.getContainer().addElem(getShip());
	}

	@Override
	public String getDescription()
	{
		return "deploying from " + aContainerShip.getShipType() + "'s cargo hold";
	}

	@Override
	protected boolean isValidShipAction()
	{
		// check that the cargo ship is in a solar system
		if (!(aContainerShip.getContainer() instanceof SolarSystem)) {
			return false;
		}
		final SolarSystem ss = (SolarSystem) aContainerShip.getContainer();
		// check solar system for availability
		return !ss.isOccupied(aDestination);
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

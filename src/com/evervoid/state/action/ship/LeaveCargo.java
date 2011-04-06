package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVContainer;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Prop;
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

	public LeaveCargo(final Ship ship) throws IllegalEVActionException
	{
		super(ship);
		aContainerShip = (Ship) ship.getContainer();
		GridLocation openLoc = null;
		final SolarSystem dest = (SolarSystem) aContainerShip.getContainer();
		for (final GridLocation loc : aContainerShip.getNeighbors(getShip().getDimension())) {
			if (!dest.isOccupied(loc)) {
				openLoc = loc;
				break;
			}
		}
		aDestination = openLoc;
		if (aDestination == null) {
			throw new IllegalEVActionException("no open deployment locations");
		}
	}

	@Override
	protected void executeAction()
	{
		final EVContainer<Prop> prevContainer = aContainerShip.getContainer();
		// leave cargo
		getShip().leaveContainer();
		// enter new
		getShip().enterContainer(prevContainer, aDestination);
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

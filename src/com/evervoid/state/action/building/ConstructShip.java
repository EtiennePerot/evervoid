package com.evervoid.state.action.building;

import java.util.Iterator;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.building.Building;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Ship;

public class ConstructShip extends BuildingAction
{
	private final Ship aShip;
	private final SolarSystem aSolarSystem;

	/**
	 * This should only be done on the Server side. If it is not, problems arise around ShipIDs.
	 * 
	 * @param player
	 * @param planet
	 * @param shipType
	 * @param state
	 * @throws IllegalEVActionException
	 */
	public ConstructShip(final Building building, String shipType, final EVGameState state) throws IllegalEVActionException
	{
		super(state, building);
		// FIXME - pull data from argument, not state
		shipType = getSender().getRaceData().getShipTypes().iterator().next();
		aSolarSystem = (SolarSystem) getPlanet().getContainer();
		// get the first available location neighboring the planet
		final Dimension shipDimension = getSender().getRaceData().getShipData(shipType).getDimension();
		final Iterator<GridLocation> locationSet = aSolarSystem.getNeighbours(getPlanet().getLocation(), shipDimension)
				.iterator();
		if (aSolarSystem.getNeighbours(getPlanet().getLocation(), shipDimension).isEmpty()) {
			throw new IllegalEVActionException("No room to construct ships");
		}
		GridLocation location = null;
		do {
			if (!locationSet.hasNext()) {
				throw new IllegalEVActionException("No room to construct ships");
			}
			location = locationSet.next();
		}
		while (aSolarSystem.isOccupied(location));
		// create a new ship at that location
		aShip = new Ship(state.getNextPropID(), getSender(), getPlanet().getContainer(), location, shipType, getState());
	}

	public ConstructShip(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aSolarSystem = (SolarSystem) getPlanet().getContainer();
		aShip = new Ship(j.getAttribute("ship"), state);
	}

	@Override
	protected void executeAction()
	{
		getState().registerProp(aShip, aSolarSystem);
	}

	@Override
	public String getDescription()
	{
		return "starting construction of a " + aShip.getShipType();
	}

	@Override
	public boolean isValidBuildingAction()
	{
		return !aSolarSystem.isOccupied(aShip.getLocation());
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("ship", aShip);
		return j;
	}
}

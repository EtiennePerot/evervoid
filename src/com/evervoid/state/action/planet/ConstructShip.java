package com.evervoid.state.action.planet;

import java.util.Iterator;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Ship;

public class ConstructShip extends PlanetAction
{
	private final Ship aShip;
	private final SolarSystem aSolarSystem;

	public ConstructShip(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aSolarSystem = (SolarSystem) getPlanet().getContainer();
		aShip = new Ship(j.getAttribute("ship"), state.getPlayerByName(j.getStringAttribute("player")), state);
	}

	/**
	 * This should only be done on the Server side. If it is not, problems arise around ShipIDs.
	 * 
	 * @param player
	 * @param planet
	 * @param shipType
	 * @param state
	 * @throws IllegalEVActionException
	 */
	public ConstructShip(final Planet planet, String shipType, final EVGameState state) throws IllegalEVActionException
	{
		super(planet.getPlayer(), planet, state);
		// FIXME - pull data from argument, not state
		shipType = aPlayer.getRaceData().getShipTypes().iterator().next();
		aSolarSystem = (SolarSystem) getPlanet().getContainer();
		// get the first available location neighboring the planet
		final Dimension shipDimension = aPlayer.getRaceData().getShipData(shipType).getDimension();
		final Iterator<GridLocation> locationSet = aSolarSystem.getNeighbours(planet.getLocation(), shipDimension).iterator();
		if (aSolarSystem.getNeighbours(planet.getLocation(), shipDimension).isEmpty()) {
			throw new IllegalEVActionException("no room to construct ships");
		}
		GridLocation location = null;
		do {
			if (!locationSet.hasNext()) {
				throw new IllegalEVActionException("no room to construct ships");
			}
			location = locationSet.next();
		}
		while (aSolarSystem.isOccupied(location));
		// create a new ship at that location
		aShip = new Ship(state.getNextPropID(), aPlayer, planet.getContainer(), location, shipType, aState);
	}

	@Override
	public void execute()
	{
		aState.addProp(aShip, aSolarSystem);
	}

	@Override
	public boolean isValidPlanetAction()
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

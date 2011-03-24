package com.evervoid.state.action.planet;

import java.util.Iterator;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.player.Player;
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

	public ConstructShip(final Player player, final Planet planet, String shipType, final EVGameState state)
			throws IllegalEVActionException
	{
		super(player, "ConstructShip", planet, state);
		// FIXME - pull data from argument, not state
		shipType = player.getRaceData().getShipTypes().iterator().next();
		aSolarSystem = (SolarSystem) getPlanet().getContainer();
		// get the first available location neighboring the planet
		final Dimension shipDimension = player.getRaceData().getShipData(shipType).getDimension();
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
		aShip = new Ship(state.getNextPropID(), player, planet.getContainer(), location, shipType, aState);
	}

	@Override
	public void execute()
	{
		aPlayer.getResources().remove(aShip.getCost());
		aState.addProp(aShip, aSolarSystem);
	}

	@Override
	public boolean isValidPlanetAction()
	{
		return !aSolarSystem.isOccupied(aShip.getLocation()) && aPlayer.hasResources(aShip.getCost());
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("ship", aShip);
		return j;
	}
}

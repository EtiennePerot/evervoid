package com.evervoid.state.action.planet;

import java.util.Iterator;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Ship;

public class ConstructShip extends PlanetAction
{
	final Ship aShip;
	final SolarSystem aSolarSystem;

	public ConstructShip(final Json j, final EVGameState state)
	{
		super(j, state);
		aSolarSystem = (SolarSystem) aPlanet.getContainer();
		aShip = new Ship(j.getAttribute("ship"), state);
	}

	public ConstructShip(final Player player, final Planet planet, final String shipType, final EVGameState state)
	{
		super(player, "ConstructShip", planet);
		aSolarSystem = (SolarSystem) aPlanet.getContainer();
		// get the first available location neighboring the planet
		final Dimension shipDimension = player.getRaceData().getShipData(shipType).getDimension();
		final Iterator<GridLocation> locationSet = aSolarSystem.getNeighbours(planet.getLocation(), shipDimension).iterator();
		GridLocation location = null;
		do {
			location = locationSet.next();
		}
		while (locationSet.hasNext() && aSolarSystem.isOccupied(location));
		// TODO - if location is null, throw some kind of a noLocation exception
		// create a new shit at that location
		aShip = new Ship(player, location, shipType, state);
	}

	@Override
	public void execute()
	{
		aSolarSystem.addElem(aShip);
	}

	@Override
	public boolean isValid()
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

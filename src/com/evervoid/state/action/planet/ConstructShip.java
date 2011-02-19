package com.evervoid.state.action.planet;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
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

	public ConstructShip(final Player player, final Planet planet, final Ship ship)
	{
		super(player, "ConstructShip", planet);
		aSolarSystem = (SolarSystem) aPlanet.getContainer();
		aShip = ship;
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

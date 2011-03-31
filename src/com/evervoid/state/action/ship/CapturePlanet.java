package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Planet;

public class CapturePlanet extends ShipAction
{
	private final Planet aPlanet;

	public CapturePlanet(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aPlanet = (Planet) state.getPropFromID(j.getIntAttribute("targetPlanet"));
	}

	@Override
	protected void executeAction()
	{
		aPlanet.changeOwner(aPlayer);
	}

	@Override
	public String getDescription()
	{
		return "capturing planet of type " + aPlanet.getPlanetType();
	}

	@Override
	protected boolean isValidShipAction()
	{
		// 1. planet owned by null player
		// 2. in the same solar system as planet
		// 3. TODO neighbor of the planet
		return aPlanet.getPlayer().equals(aState.getNullPlayer()) && aPlanet.getContainer().equals(getShip().getContainer());
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("targetPlanet", aPlanet.getID());
		return j;
	}
}

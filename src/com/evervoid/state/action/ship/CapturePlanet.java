package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Ship;

public class CapturePlanet extends ShipAction
{
	private final Planet aPlanet;

	public CapturePlanet(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aPlanet = (Planet) state.getPropFromID(j.getIntAttribute("targetPlanet"));
	}

	public CapturePlanet(final Planet planet, final Ship ship, final EVGameState state) throws IllegalEVActionException
	{
		super(ship, state);
		aPlanet = planet;
	}

	@Override
	protected void executeAction()
	{
		aPlanet.changeOwner(getSender());
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
		// 3. TODO enforce that ship has to be a neighbor of the planet
		return aPlanet.getPlayer().equals(getState().getNullPlayer())
				&& aPlanet.getContainer().equals(getShip().getContainer());
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("targetPlanet", aPlanet.getID());
		return j;
	}
}

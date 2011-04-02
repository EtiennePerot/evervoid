package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Ship;

public class CapturePlanet extends ShipAction
{
	private final Planet aTargetPlanet;

	public CapturePlanet(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aTargetPlanet = (Planet) state.getPropFromID(j.getIntAttribute("targetPlanet"));
	}

	public CapturePlanet(final Planet planet, final Ship ship, final EVGameState state) throws IllegalEVActionException
	{
		super(ship, state);
		aTargetPlanet = planet;
	}

	@Override
	protected void executeAction()
	{
		aTargetPlanet.changeOwner(getSender());
	}

	@Override
	public String getDescription()
	{
		return "capturing planet of type " + aTargetPlanet.getPlanetType();
	}

	public Planet getTarget()
	{
		return aTargetPlanet;
	}

	@Override
	protected boolean isValidShipAction()
	{
		// 1. planet owned by null player
		// 2. in the same solar system as planet
		// 3. TODO enforce that ship has to be a neighbor of the planet
		return aTargetPlanet.getPlayer().equals(getState().getNullPlayer())
				&& aTargetPlanet.getContainer().equals(getShip().getContainer());
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("targetPlanet", aTargetPlanet.getID());
		return j;
	}
}

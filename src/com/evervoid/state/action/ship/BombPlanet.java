package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Planet;

public class BombPlanet extends ShipAction
{
	private final Planet aTargetPlanet;

	public BombPlanet(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aTargetPlanet = (Planet) state.getPropFromID(j.getIntAttribute("bombingTarget"));
	}

	@Override
	protected void executeAction()
	{
		// TODO bomb planet
	}

	@Override
	public String getDescription()
	{
		return "bombing planet ";
	}

	@Override
	protected boolean isValidShipAction()
	{
		// 1. is not dead
		// 2. is in the correct solar system
		// 3. is within reach
		return !getShip().isDead() && getShip().getContainer().equals(aTargetPlanet.getContainer())
				&& getShip().canShoot(aTargetPlanet);
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("bombingTarget", aTargetPlanet.getID());
		return j;
	}
}

package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Ship;

public class BombPlanet extends ShipAction
{
	// FIXME - not 5
	private int aDamage = 5;
	private final Planet aTargetPlanet;

	public BombPlanet(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aTargetPlanet = (Planet) state.getPropFromID(j.getIntAttribute("bombingTarget"));
		aDamage = j.getIntAttribute("damage");
	}

	public BombPlanet(final Planet planet, final Ship ship, final EVGameState state) throws IllegalEVActionException
	{
		super(ship, state);
		aTargetPlanet = planet;
	}

	@Override
	protected void executeAction()
	{
		aTargetPlanet.takeDamange(aDamage);
	}

	public int getDamage()
	{
		return aDamage;
	}

	@Override
	public String getDescription()
	{
		return "bombing planet ";
	}

	@Override
	protected boolean isValidShipAction()
	{
		// 1. is in the correct solar system
		// 2. is within reach
		return getShip().getContainer().equals(aTargetPlanet.getContainer()) && getShip().canShoot(aTargetPlanet);
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("bombingTarget", aTargetPlanet.getID());
		j.setIntAttribute("damage", aDamage);
		return j;
	}
}

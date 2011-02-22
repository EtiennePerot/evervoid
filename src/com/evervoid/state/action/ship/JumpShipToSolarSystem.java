package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Ship;

public class JumpShipToSolarSystem extends ShipAction
{
	final SolarSystem aDestination;
	final GridLocation aLocation;

	public JumpShipToSolarSystem(final Json j, final EVGameState state)
	{
		super(j, state);
		aDestination = state.getSolarSystem(j.getIntAttribute("ssid"));
		aLocation = new GridLocation(j.getAttribute("location"));
	}

	public JumpShipToSolarSystem(final Player player, final Ship ship, final SolarSystem destination)
	{
		super(player, "JumpShip", ship);
		aDestination = destination;
		// TODO - decide on a real location
		aLocation = new GridLocation(0, 0, ship.getData().getDimension());
	}

	public boolean destinationFree()
	{
		return !aDestination.isOccupied(aLocation);
	}

	@Override
	public void execute()
	{
		aShip.jumpToSolarSystem(aDestination, aLocation);
	}

	@Override
	public boolean isValid()
	{
		return destinationFree();
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("ssid", aDestination.getID());
		j.setAttribute("location", aLocation);
		return j;
	}
}

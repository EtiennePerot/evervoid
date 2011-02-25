package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Portal;
import com.evervoid.state.prop.Ship;

public class JumpShipToSolarSystem extends ShipAction
{
	final SolarSystem aDestination;
	final GridLocation aLocation;
	final MoveShip aUnderlyingMove;

	public JumpShipToSolarSystem(final Json j, final EVGameState state)
	{
		super(j, state);
		aDestination = state.getSolarSystem(j.getIntAttribute("ssid"));
		aLocation = new GridLocation(j.getAttribute("location"));
		aUnderlyingMove = new MoveShip(j.getAttribute("movement"), state);
	}

	public JumpShipToSolarSystem(final Ship ship, final Portal portal)
	{
		super("JumpShip", ship);
		final Dimension shipDim = ship.getData().getDimension();
		aUnderlyingMove = new MoveShip(ship.getPlayer(), ship, portal.getJumpingLocation(shipDim));
		aDestination = portal.getDestination();
		// TODO - decide on a real location
		aLocation = new GridLocation(0, 0, shipDim);
	}

	public boolean destinationFree()
	{
		return !aDestination.isOccupied(aLocation);
	}

	@Override
	public void execute()
	{
		aShip.jumpToSolarSystem(aDestination, aUnderlyingMove.getPath(), aLocation);
	}

	@Override
	public boolean isValid()
	{
		return aUnderlyingMove.isValid() && destinationFree();
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("ssid", aDestination.getID());
		j.setAttribute("location", aLocation);
		j.setAttribute("movement", aUnderlyingMove);
		return j;
	}
}

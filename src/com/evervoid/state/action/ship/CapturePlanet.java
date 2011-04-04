package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Ship;

public class CapturePlanet extends ShipAction
{
	private final Planet aTargetPlanet;
	private final MoveShip aUnderlyingMove;

	public CapturePlanet(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aTargetPlanet = (Planet) state.getPropFromID(j.getIntAttribute("targetPlanet"));
		aUnderlyingMove = new MoveShip(j.getAttribute("movement"), state);
	}

	public CapturePlanet(final Planet planet, final Ship ship, final EVGameState state) throws IllegalEVActionException
	{
		super(ship, state);
		aTargetPlanet = planet;
		final GridLocation closestLocation = ship.getLocation().getClosestOrigin(
				aTargetPlanet.getNeighborOrigins(getShip().getDimension()));
		aUnderlyingMove = new MoveShip(ship, closestLocation.origin, getState());
		if (!aUnderlyingMove.isValid()) {
			throw new IllegalEVActionException("bad underlying move");
		}
	}

	@Override
	protected void executeAction()
	{
		getShip().move(aUnderlyingMove.getDestination(), aUnderlyingMove.getFinalPath());
		getShip().capturePlanet(aTargetPlanet, aUnderlyingMove.getFinalPath());
	}

	@Override
	public String getDescription()
	{
		return "Capturing planet of type " + aTargetPlanet.getPlanetType();
	}

	public Planet getTarget()
	{
		return aTargetPlanet;
	}

	public MoveShip getUnderlyingMove()
	{
		return aUnderlyingMove;
	}

	@Override
	protected boolean isValidShipAction()
	{
		// 1. planet owned by null player
		// 2. in the same solar system as planet
		// 3. can move to planet
		return aTargetPlanet.getPlayer().equals(getState().getNullPlayer())
				&& aTargetPlanet.getContainer().equals(getShip().getContainer()) && aUnderlyingMove.isValidShipAction();
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("targetPlanet", aTargetPlanet.getID());
		j.setAttribute("movement", aUnderlyingMove);
		return j;
	}
}

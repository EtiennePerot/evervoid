package com.evervoid.state.action.ship;

import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.prop.Portal;
import com.evervoid.state.prop.Ship;
import com.evervoid.utils.MathUtils;

public class JumpShipIntoPortal extends ShipAction
{
	private final Portal aDestination;
	private final GridLocation aDestLocation;
	private final Portal aPortal;
	private final MoveShip aUnderlyingMove;

	public JumpShipIntoPortal(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aDestination = (Portal) state.getPropFromID(j.getIntAttribute("destPortal"));
		aPortal = (Portal) state.getPropFromID(j.getIntAttribute("portal"));
		aDestLocation = new GridLocation(j.getAttribute("destLoc"));
		aUnderlyingMove = new MoveShip(j.getAttribute("movement"), state);
	}

	public JumpShipIntoPortal(final Ship ship, final Portal portal, final EVGameState state) throws IllegalEVActionException
	{
		super(ship, state);
		final Dimension shipDim = ship.getData().getDimension();
		aPortal = portal;
		final GridLocation closestJump = ship.getLocation().getClosestOrigin(portal.getJumpingLocations(shipDim));
		aUnderlyingMove = new MoveShip(ship, closestJump.origin, aState);
		aDestination = aPortal.getWormhole().getOtherPortal(portal);
		final Set<Point> possibleLocations = aDestination.getJumpingLocations(ship.getDimension());
		GridLocation tempLocation;
		do {
			if (possibleLocations.isEmpty()) {
				throw new IllegalEVActionException("no valid jump exit locations ");
			}
			tempLocation = new GridLocation((Point) MathUtils.getRandomElement(possibleLocations), ship.getDimension());
			possibleLocations.remove(tempLocation.origin);
		}
		while (aDestination.getContainer().isOccupied(tempLocation));
		aDestLocation = tempLocation;
	}

	public boolean destinationFree()
	{
		return !aDestination.getContainer().isOccupied(aDestLocation);
	}

	@Override
	protected void executeAction()
	{
		getShip().jumpToSolarSystem(aDestination.getContainer(), aUnderlyingMove.getDestination(),
				aUnderlyingMove.getSamplePath(), aDestLocation, aPortal);
	}

	public Portal getPortal()
	{
		return aPortal;
	}

	public MoveShip getUnderlyingMove()
	{
		return aUnderlyingMove;
	}

	@Override
	public boolean isValidShipAction()
	{
		return aUnderlyingMove.isValid() && destinationFree() && getShip().canJump();
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("destPortal", aDestination.getID());
		j.setAttribute("destLoc", aDestLocation);
		j.setAttribute("movement", aUnderlyingMove);
		j.setIntAttribute("portal", aPortal.getID());
		return j;
	}
}

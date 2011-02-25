package com.evervoid.state.action.ship;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.prop.Portal;
import com.evervoid.state.prop.Ship;

public class JumpShipToSolarSystem extends ShipAction
{
	private final SolarSystem aDestination;
	private final GridLocation aLocation;
	private final Portal aPortal;
	private final MoveShip aUnderlyingMove;

	public JumpShipToSolarSystem(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aDestination = state.getSolarSystem(j.getIntAttribute("ssid"));
		aPortal = (Portal) state.getPropFromID(j.getIntAttribute("portal"));
		aLocation = new GridLocation(j.getAttribute("location"));
		aUnderlyingMove = new MoveShip(j.getAttribute("movement"), state);
	}

	public JumpShipToSolarSystem(final Ship ship, final Portal portal) throws IllegalEVActionException
	{
		super("JumpShip", ship);
		final Dimension shipDim = ship.getData().getDimension();
		aPortal = portal;
		final GridLocation closestJump = ship.getLocation().getClosest(portal.getJumpingLocations(shipDim));
		aUnderlyingMove = new MoveShip(ship, closestJump.origin);
		aDestination = aPortal.getDestination();
		// TODO - decide on a real location
		try {
			aLocation = new GridLocation((Point) MathUtils.getRandomElement(portal.getJumpingLocations(ship.getDimension())),
					ship.getDimension());
		}
		catch (final NullPointerException e) {
			throw new IllegalEVActionException("no valid jump exit locations");
		}
	}

	public boolean destinationFree()
	{
		return !aDestination.isOccupied(aLocation);
	}

	@Override
	public void execute()
	{
		aShip.jumpToSolarSystem(aDestination, aUnderlyingMove.getPath(), aLocation, aPortal);
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
		j.setIntAttribute("portal", aPortal.getID());
		return j;
	}
}

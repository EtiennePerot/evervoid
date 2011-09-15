package com.evervoid.state.action.ship;

import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.Wormhole;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.prop.Portal;
import com.evervoid.state.prop.Ship;
import com.evervoid.utils.MathUtils;

/**
 * Jumps a {@link Ship} into a {@link Portal} to go through the linked {@link Wormhole}. The Ship must have enough radiation to
 * jump and be close enough to move to the portal.
 */
public class JumpShipIntoPortal extends ShipAction
{
	/**
	 * The destination portal.
	 */
	private final Portal aDestination;
	/**
	 * The exit location in the destination SolarSystem.
	 */
	private final GridLocation aDestLocation;
	/**
	 * The Portal through which the jump occurs.
	 */
	private final Portal aPortal;
	/**
	 * The move the ship makes to get to the portal.
	 */
	private final MoveShip aUnderlyingMove;

	/**
	 * Json deserializer.
	 * 
	 * @param j
	 *            The Json serialization of the action.
	 * @param state
	 *            The state on which this action will be executed.
	 * @throws IllegalEVActionException
	 *             If the action is not legal.
	 */
	public JumpShipIntoPortal(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aDestination = (Portal) state.getPropFromID(j.getIntAttribute("destPortal"));
		aPortal = (Portal) state.getPropFromID(j.getIntAttribute("portal"));
		aDestLocation = new GridLocation(j.getAttribute("destLoc"));
		aUnderlyingMove = new MoveShip(j.getAttribute("movement"), state);
	}

	/**
	 * Creates a new JumpShipIntoPortal action.
	 * 
	 * @param ship
	 *            The jumping Ship.
	 * @param portal
	 *            The portal through which to jump.
	 * @throws IllegalEVActionException
	 *             If the action is not valid.
	 */
	public JumpShipIntoPortal(final Ship ship, final Portal portal) throws IllegalEVActionException
	{
		super(ship);
		final Dimension shipDim = ship.getData().getDimension();
		aPortal = portal;
		final GridLocation closestJump = ship.getLocation().getClosestOrigin(portal.getJumpingLocations(shipDim));
		aUnderlyingMove = new MoveShip(ship, closestJump.origin);
		aDestination = aPortal.getWormhole().getOtherPortal(portal);
		final Set<Point> possibleLocations = aDestination.getJumpingLocations(ship.getDimension());
		GridLocation tempLocation;
		do {
			if (possibleLocations.isEmpty()) {
				throw new IllegalEVActionException("No valid jump exit locations ");
			}
			tempLocation = new GridLocation(MathUtils.getRandomElement(possibleLocations), ship.getDimension());
			possibleLocations.remove(tempLocation.origin);
		}
		while (aDestination.getContainer().isOccupied(tempLocation));
		aDestLocation = tempLocation;
	}

	/**
	 * @return Whether the current destination is free.
	 */
	public boolean destinationFree()
	{
		return !aDestination.getContainer().isOccupied(aDestLocation);
	}

	@Override
	protected void executeAction()
	{
		getShip().jumpToSolarSystem(aDestination.getContainer(), aUnderlyingMove.getDestination(),
				aUnderlyingMove.getSamplePath(), aDestLocation);
	}

	@Override
	public String getDescription()
	{
		return "Jumping towards\n" + (aPortal.getDestinationPortal().getContainer()).getName();
	}

	/**
	 * @return The portal into which the Ship is jumpin.g
	 */
	public Portal getPortal()
	{
		return aPortal;
	}

	/**
	 * @return The move the Ship must carry out in order to jump into the portal.
	 */
	public MoveShip getUnderlyingMove()
	{
		return aUnderlyingMove;
	}

	@Override
	public boolean isValidShipAction()
	{
		return aUnderlyingMove.isValidShipAction() && getShip().canJump()
				&& !aDestination.getSolarSystem().isOccupied(aDestLocation);
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("destPortal", aDestination.getID());
		j.setAttribute("destLoc", aDestLocation);
		j.setAttribute("movement", aUnderlyingMove);
		j.setAttribute("portal", aPortal.getID());
		return j;
	}
}

package com.evervoid.state.action.ship;

import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Ship;

/**
 * CapturePlanet is an action in which a {@link Ship} captures a {@link Planet}. The Planet must be currently owned by the
 * Neutral Player, and must be within reach of the Ship (which must be able to capture Planets).
 */
public class CapturePlanet extends ShipAction
{
	/**
	 * The Planet to capture.
	 */
	private final Planet aTargetPlanet;
	/**
	 * The move the Ship must make in order to reach the Planet.
	 */
	private MoveShip aUnderlyingMove;

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
	public CapturePlanet(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aTargetPlanet = (Planet) state.getPropFromID(j.getIntAttribute("targetPlanet"));
		if (!getShip().isNeighborOf(aTargetPlanet)) {
			aUnderlyingMove = new MoveShip(j.getAttribute("movement"), state);
		}
	}

	/**
	 * Creates a new CapturePlanet action.
	 * 
	 * @param planet
	 *            The Planet being captured.
	 * @param ship
	 *            The Ship doing the capturing.
	 * @throws IllegalEVActionException
	 *             If the action is not valid.
	 */
	public CapturePlanet(final Planet planet, final Ship ship) throws IllegalEVActionException
	{
		super(ship);
		aTargetPlanet = planet;
		if (!aTargetPlanet.getPlayer().isNullPlayer()) {
			throw new IllegalEVActionException("Can only capture neutral Planets");
		}
		final GridLocation closestLocation = ship.getLocation().getClosestOrigin(
				aTargetPlanet.getFreeNeighborOrigins(getShip().getDimension()));
		if (!getShip().isNeighborOf(aTargetPlanet)) {
			aUnderlyingMove = new MoveShip(ship, closestLocation.origin);
			if (!aUnderlyingMove.isValid()) {
				throw new IllegalEVActionException("Cannot move next to Planet");
			}
		}
	}

	@Override
	protected void executeAction()
	{
		if (!getShip().isNeighborOf(aTargetPlanet)) {
			getShip().move(aUnderlyingMove.getDestination(), aUnderlyingMove.getFinalPath());
		}
		getShip().capturePlanet(aTargetPlanet);
	}

	@Override
	public String getDescription()
	{
		return "Capturing a planet";
	}

	/**
	 * @return The path that the Ship will take on its way to the Planet. Null if the Ship is already next to the Planet.
	 */
	public List<GridLocation> getSamplePath()
	{
		if (aUnderlyingMove == null) {
			return null;
		}
		return aUnderlyingMove.getSamplePath();
	}

	/**
	 * @return The Planet being captured.
	 */
	public Planet getTarget()
	{
		return aTargetPlanet;
	}

	@Override
	protected boolean isValidShipAction()
	{
		// 1. planet owned by null player
		// 2. in the same solar system as planet
		// 3. can move to planet
		final boolean moveValid = getShip().isNeighborOf(aTargetPlanet) || aUnderlyingMove.isValid();
		return aTargetPlanet.getPlayer().equals(getState().getNullPlayer())
				&& aTargetPlanet.getContainer().equals(getShip().getContainer()) && moveValid;
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("targetPlanet", aTargetPlanet.getID());
		j.setAttribute("movement", aUnderlyingMove);
		return j;
	}
}

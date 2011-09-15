package com.evervoid.state.action.ship;

import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.evervoid.state.prop.Pathfinder;
import com.evervoid.state.prop.Ship;
import com.evervoid.state.prop.ShipPath;

/**
 * Moves a {@link Ship} from one location in a {@link SolarSystem} to another one within that same SolarSystem. The Ship must be
 * able to make the move. Once created, the path should never change.
 */
public class MoveShip extends ShipAction
{
	/**
	 * The ship's destination.
	 */
	private GridLocation aDestination;
	/**
	 * The path the ship will take on it's way to the destination.
	 */
	private ShipPath aFinalPath = null;

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
	public MoveShip(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aDestination = new GridLocation(j.getAttribute("destination"));
	}

	/**
	 * Creates a new MoveShip Action.
	 * 
	 * @param ship
	 *            The moving Ship.
	 * @param destination
	 *            The location to which the Ship is moving.
	 * @throws IllegalEVActionException
	 *             If the action is invalid.
	 */
	public MoveShip(final Ship ship, final GridLocation destination) throws IllegalEVActionException
	{
		super(ship);
		if (ship.getDimension().equals(destination.getDimension())) {
			throw new IllegalEVActionException("Location is not big enough to hold the ship");
		}
		aDestination = destination;
	}

	/**
	 * Creates a new MoveShip Action.
	 * 
	 * @param ship
	 *            The moving Ship.
	 * @param destination
	 *            The top left point of the location to which the Ship is moving.
	 * @throws IllegalEVActionException
	 *             If the action is invalid.
	 */
	public MoveShip(final Ship ship, final Point destination) throws IllegalEVActionException
	{
		super(ship);
		aDestination = new GridLocation(destination, ship.getData().getDimension());
	}

	@Override
	public boolean execute()
	{
		if (getShip().isDead()) { // Don't even try to move it the ship is dead
			return false;
		}
		if (!isValid()) {
			aDestination = aDestination.getClosest(getShip().getValidDestinations(), getShip().getLocation());
			aFinalPath = null;
		}
		executeAction();
		return true;
	}

	@Override
	protected void executeAction()
	{
		getShip().move(aDestination, getFinalPath());
	}

	@Override
	public String getDescription()
	{
		return "is moving";
	}

	/**
	 * @return The ship's destination.
	 */
	public GridLocation getDestination()
	{
		return aDestination;
	}

	/**
	 * This will create a ShipPath if none exists. Once created the path should never change.
	 * 
	 * @return The path the Ship takes on it's way to the destination.
	 */
	public ShipPath getFinalPath()
	{
		if (aFinalPath == null) {
			aFinalPath = new ShipPath(getShip().getLocation(), aDestination, getSamplePath(), (SolarSystem) getShip()
					.getContainer());
		}
		return aFinalPath;
	}

	/**
	 * @return A POSSIBLE path for the ship to the destination. Does not correspond to the final path at all
	 */
	public List<GridLocation> getSamplePath()
	{
		return new Pathfinder().findPath(getShip(), aDestination);
	}

	@Override
	public boolean isValidShipAction()
	{
		// 1. Ship must be in a SolarSystem
		// 2. Destination is reachable
		return getShip().getContainer() instanceof SolarSystem && getShip().getValidDestinations().contains(aDestination);
	}

	@Override
	public Json toJson()
	{
		return super.toJson().setAttribute("destination", aDestination);
	}
}

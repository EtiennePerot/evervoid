package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Ship;

/**
 * The action {@link Ship} docks into the container Ship if it fits and belongs to the same {@link Player}.
 */
public class EnterCargo extends ShipAction
{
	/**
	 * The containing Ship.
	 */
	private final Ship aContainerShip;
	/**
	 * The move that aShip must make in order to dock.
	 */
	private MoveShip aUnderlyingMove;

	/**
	 * Json deserializer.
	 * 
	 * @param j
	 *            The Json serialization of the action.
	 * @param state
	 *            The state on which the action will be executed.
	 * @throws IllegalEVActionException
	 *             If the action cannot be performed.
	 */
	public EnterCargo(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aContainerShip = (Ship) state.getPropFromID(j.getIntAttribute("cargoShip"));
		aUnderlyingMove = new MoveShip(j.getAttribute("movement"), state);
	}

	/**
	 * Creates an EnterCargo action.
	 * 
	 * @param actionShip
	 *            The docking Ship.
	 * @param cargoShip
	 *            The cargo Ship.
	 * @throws IllegalEVActionException
	 *             If the action cannot be executed.
	 */
	public EnterCargo(final Ship actionShip, final Ship cargoShip) throws IllegalEVActionException
	{
		super(actionShip);
		aContainerShip = cargoShip;
		final GridLocation closestLocation = getShip().getLocation().getClosestOrigin(
				cargoShip.getFreeNeighborOrigins(getShip().getDimension()));
		aUnderlyingMove = new MoveShip(getShip(), closestLocation.origin);
	}

	@Override
	protected void executeAction()
	{
		getShip().enterCargo(aContainerShip, aUnderlyingMove.getFinalPath());
	}

	@Override
	public String getDescription()
	{
		return "Docking in cargo hold";
	}

	/**
	 * @return The Ship in which to doc.
	 */
	public Ship getTarget()
	{
		return aContainerShip;
	}

	/**
	 * @return The move associated with docking.
	 */
	public MoveShip getUnderlyingMove()
	{
		return aUnderlyingMove;
	}

	@Override
	protected boolean isValidShipAction()
	{
		// 1. Container has space
		// 2. Container is in a solar system
		// 3. No recursive cargo holders
		// 4. Both ships are in the same solar system
		// 5. Space next to container is open
		return (aContainerShip.canHold(getShip()) && getShip().getContainer() instanceof SolarSystem)
				&& getShip().getCargoCapacity() == 0 && (getShip().getContainer().equals(aContainerShip.getContainer()))
				&& aUnderlyingMove.isValid();
	}

	/**
	 * Sets the location to which the Ship moves, only if the local ship can move to that location within a turn.
	 * 
	 * @param location
	 *            The location to set.
	 * @return The final location.
	 */
	public GridLocation setDestination(final GridLocation location)
	{
		try {
			final MoveShip newShipMove = new MoveShip(getShip(), location);
			if (newShipMove.isValid()) {
				aUnderlyingMove = newShipMove;
			}
		}
		catch (final IllegalEVActionException e) {
			// clearly we can't be doing this... do nothing here, we'll just keep the old move and return it's location
		}
		return aUnderlyingMove.getDestination();
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("cargoShip", aContainerShip.getID());
		j.setAttribute("movement", aUnderlyingMove);
		return j;
	}
}

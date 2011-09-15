package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Action;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.action.PropAction;
import com.evervoid.state.prop.Ship;

/**
 * An abstract extension of {@link Action} that groups functionality common to all Actions pertaining to a particular
 * {@link Ship}. Subclasses should override isValidShipAction() and execute executeAction(). By default Ships need to be alive
 * in order to execute actions, this can be changed by overriding the requiresShipAlive() method to return false.
 */
public abstract class ShipAction extends PropAction
{
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
	public ShipAction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		if (!(getProp() instanceof Ship)) {
			throw new IllegalEVActionException("Prop is not a ship");
		}
	}

	/**
	 * Creates a new ShipAction pertaining to the parameter Ship.
	 * 
	 * @param ship
	 *            The ship carrying out the action.
	 * @throws IllegalEVActionException
	 *             If the action is not valid.
	 */
	public ShipAction(final Ship ship) throws IllegalEVActionException
	{
		super(ship.getPlayer(), ship, ship.getState());
	}

	/**
	 * @return The Ship executing the action.
	 */
	public Ship getShip()
	{
		return (Ship) getProp();
	}

	/**
	 * Check if this ShipAction is valid. Calls the template method isValidShipAction iff ship is valid in the first place.
	 * Subclasses should only override isValidShipAction, hence the "final" keyword on this method.
	 */
	@Override
	protected final boolean isValidPropAction()
	{
		return getProp() instanceof Ship && (!requireShipAlive() || !getShip().isDead()) && isValidShipAction();
	}

	/**
	 * @return Whether the ShipAction is valid and legal to be execute on its state.
	 */
	protected abstract boolean isValidShipAction();

	/**
	 * @return Whether this action should only be executed if the Ship is alive.
	 */
	protected boolean requireShipAlive()
	{
		return true;
	}
}

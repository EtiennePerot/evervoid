package com.evervoid.state.observers;

import com.evervoid.state.EVContainer;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.evervoid.state.prop.ShipPath;

/**
 * Ships call these functions all all their observers when a predetermined event happens. Objects may receive these messages by
 * registering themselves to ship ( by calling registerObserver(ShipObserver) ). The first argument of these methods should
 * always be the calling ship; this allows Objects to observer multiple ships at the same time. Note that the method call always
 * implies that the ship is carrying out an action and NOT that an action is being carried out on the ship.
 */
public interface ShipObserver
{
	/**
	 * Called when a ship bombs a planet.
	 * 
	 * @param ship
	 *            The ship doing the bombing.
	 * @param bombLocation
	 *            The location of the target.
	 */
	public void shipBombed(Ship ship, GridLocation bombLocation);

	/**
	 * Called when a ship is destroyed.
	 * 
	 * @param ship
	 *            The ship destroyed.
	 */
	public void shipDestroyed(Ship ship);

	/**
	 * Called when a ship enters another ship's cargo bay.
	 * 
	 * @param container
	 *            The ship whose cargo bay is being entered.
	 * @param docker
	 *            The ship docking into the cargo bay.
	 */
	public void shipEnteredCargo(Ship container, Ship docker);

	/**
	 * Called when ship exits the cargo bay of another ship.
	 * 
	 * @param container
	 *            The ship whose cargo bay is being freed up.
	 * @param docker
	 *            The ship deploying from the cargo bay.
	 */
	public void shipExitedCargo(Ship container, Ship docker);

	/**
	 * Called when a ship's health changes.
	 * 
	 * @param ship
	 *            The ship whose health is changing.
	 * @param health
	 *            The amount by which the ship's health changed; this may be negative.
	 */
	public void shipHealthChanged(Ship ship, int health);

	/**
	 * Called when a ship jumps out of a container.
	 * 
	 * @param ship
	 *            The jumping ship.
	 * @param oldContainer
	 *            The origin container.
	 * @param leavingMove
	 *            The path necessary to arrive at the jump location; this may be null.
	 * @param newContainer
	 *            The destination container.
	 */
	public void shipJumped(Ship ship, EVContainer<Prop> oldContainer, ShipPath leavingMove, EVContainer<Prop> newContainer);

	public void shipLeftContainer(Ship ship, EVContainer<Prop> container, ShipPath exitPath);

	/**
	 * Called when a ship moves from one location to another within a container.
	 * 
	 * @param ship
	 *            The moving ship.
	 * @param oldLocation
	 *            The origin of the ship.
	 * @param path
	 *            The path associated with the move.
	 */
	public void shipMoved(Ship ship, GridLocation oldLocation, ShipPath path);

	/**
	 * Called when a ship shoots another.
	 * 
	 * @param ship
	 *            The shooting ship.
	 * @param shootLocation
	 *            The location being shot.
	 */
	public void shipShot(Ship ship, GridLocation shootLocation);
}

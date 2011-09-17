package com.evervoid.state.observers;

import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.evervoid.state.prop.ShipPath;
import com.evervoid.utils.EVContainer;

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
	 * Called when a ship captures a neutral planet
	 * 
	 * @param ship
	 *            The ship doing the capturing.
	 * @param planet
	 *            The planet being captured.
	 */
	public void shipCapturedPlanet(Ship ship, final Planet planet);

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
	 * @param ship
	 *            The ship docking.
	 * @param containerShip
	 *            The ship whose cargo bay is being docked into.
	 * @param shipPath
	 *            The path associated with docking.
	 */
	public void shipEnteredCargo(Ship ship, Ship containerShip, ShipPath shipPath);

	/**
	 * Called when another ship enters this ship's cargo bay.
	 * 
	 * @param containerShip
	 *            The ship whose cargo bay is being docked into.
	 * @param ship
	 *            The ship docking.
	 */
	public void shipEnteredThis(Ship containerShip, Ship ship);

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

	/**
	 * Called when a Ship leaves its container.
	 * 
	 * @param ship
	 *            The ship leaving.
	 * @param container
	 *            The container being left.
	 * @param exitPath
	 *            The path taken on the way out of the container.
	 */
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
	 * Called when a ship's shields change.
	 * 
	 * @param ship
	 *            The ship whose health is changing.
	 * @param shields
	 *            The amount by which the ship's shields changed; this may be negative.
	 */
	public void shipShieldsChanged(Ship ship, int shields);

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

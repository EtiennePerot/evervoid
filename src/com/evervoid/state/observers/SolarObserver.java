package com.evervoid.state.observers;

import com.evervoid.state.SolarSystem;
import com.evervoid.state.prop.Ship;

/**
 * SolarObserver is a template for Objects wishing to observe {@link SolarSystem} and their actions. SolarSystems will broadcast
 * to all their observers by calling each method when appropriate. The first parameter of the broadcasting method is the
 * SolarSystem that is being observed, this allows Objects to observe multiple SolarSystems at once.
 */
public interface SolarObserver
{
	/**
	 * Called when a new ship enters the broadcasting SolarSystem.
	 * 
	 * @param broadcastingSS
	 *            The SolarSystem into which the Ship is entering.
	 * @param newShip
	 *            The Ship that has just entered the SolarSystem.
	 */
	public void shipEntered(SolarSystem broadcastingSS, Ship newShip);

	/**
	 * Called when a Ship leaves the broadcasting SolarSystem.
	 * 
	 * @param broadcastingSS
	 *            The SolarSystem from which the Ship has left.
	 * @param oldShip
	 *            The Ship leaving the SolarSytsem.
	 */
	public void shipLeft(SolarSystem broadcastingSS, Ship oldShip);
}

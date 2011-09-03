package com.evervoid.state.observers;

import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;

/**
 * PlanetObserver is a template for classes wishing to observe the actions of a {@link Planet}. Any class implementing
 * PlanetObserver can register itself to a Planet. Planets will then call these methods on all Objects observing them. The first
 * parameter should always be the Planet that is broadcasting to its observers; this is to allow Objects to observe multiple
 * Planets simultaneously.
 */
public interface PlanetObserver
{
	/**
	 * Called when the broadcastingPlanet has constructed, destroyed, or changed a Building.
	 * 
	 * @param broadcastingPlanet
	 *            The Planet whose Buildings have changed.
	 */
	public void buildingsChanged(Planet broadcastingPlanet);

	/**
	 * Called when the broadcastingPlanet's health has changed.
	 * 
	 * @param broadcastingPlanet
	 *            The Planet whose health has changed.
	 * @param delta
	 *            The amount by which the health changed.
	 */
	public void healthChanged(Planet broadcastingPlanet, int delta);

	/**
	 * Called when the broadcastingPlanet has changed owners. The old owner is passed for reference, the new owner can be found
	 * by using Planet.getPlayer() .
	 * 
	 * @param broadcastingPlanet
	 *            The planet being captured
	 * @param oldOwner
	 *            The Planet's old owner, should not be the same as the new owner.
	 */
	public void planetCaptured(Planet broadcastingPlanet, Player oldOwner);

	/**
	 * Called when the broadcastingPlanet's shield have changed.
	 * 
	 * @param broadcastingPlanet
	 *            The Planet whose shields have changed.
	 * @param delta
	 *            The amount by which the shield have changed.
	 */
	public void shieldsChanged(Planet broadcastingPlanet, int delta);
}

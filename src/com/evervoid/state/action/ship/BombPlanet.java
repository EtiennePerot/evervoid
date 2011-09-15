package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Ship;
import com.evervoid.utils.MathUtils;

/**
 * Bomb Planet lets a {@link Ship} deal damage to a {@link Planet}. It checks that the Ship is within range of the Planet, that
 * the Ship can shoot Planets, and that the Planet belongs to an enemy Player. If all conditions are met, it removes the
 * appropriate amount of shields and health.
 */
public class BombPlanet extends ShipAction
{
	// FIXME - not 15
	/**
	 * The damage done by this bombin.g
	 */
	private int aDamage = 15;
	/**
	 * The Planet being bombed.
	 */
	private final Planet aTargetPlanet;

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
	public BombPlanet(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aTargetPlanet = (Planet) state.getPropFromID(j.getIntAttribute("bombingTarget"));
		aDamage = j.getIntAttribute("damage");
	}

	/**
	 * Creates a BombPlanet action in which the parameter Ship bombs the parameter Planet.
	 * 
	 * @param planet
	 *            The Planet to bomb.
	 * @param ship
	 *            The Ship doing the bombing.
	 * @throws IllegalEVActionException
	 *             If the Ship cannot bomb the Planet.
	 */
	public BombPlanet(final Planet planet, final Ship ship) throws IllegalEVActionException
	{
		super(ship);
		if (planet.getPlayer().isNullPlayer()) {
			throw new IllegalEVActionException("Canot bomb neutral planets");
		}
		aTargetPlanet = planet;
	}

	@Override
	protected void executeAction()
	{
		aTargetPlanet.takeDamange(aDamage);
	}

	/**
	 * @return The damage being done by this action.
	 */
	public int getDamage()
	{
		return aDamage;
	}

	@Override
	public String getDescription()
	{
		return "Bombing a planet";
	}

	/**
	 * @return The Planet being bombed.
	 */
	public Planet getTarget()
	{
		return aTargetPlanet;
	}

	@Override
	protected boolean isValidShipAction()
	{
		// 1. Planet is owned by enemy
		// 2. Planet is not owned by nullplayer
		// 3. is in the correct solar system
		// 4. is within reach
		final Player owner = aTargetPlanet.getPlayer();
		if (owner.isNullPlayer() || owner.equals(getSender())) {
			return false;
		}
		return getShip().getContainer().equals(aTargetPlanet.getContainer()) && getShip().canShoot(aTargetPlanet);
	}

	/**
	 * Sets the damage to a random value. Will only do so if the current damage is zero (in order to keep consistent damage).
	 */
	public void rollDamage()
	{
		if (aDamage == 0) {
			aDamage = MathUtils.getRandomIntBetween(0, getShip().getMaxDamage());
		}
		// TODO: Do it
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("bombingTarget", aTargetPlanet.getID());
		j.setAttribute("damage", aDamage);
		return j;
	}
}

package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.evervoid.utils.MathUtils;

/**
 * An action that lets one {@link Ship} shoot an opponent Ship.
 */
public class ShootShip extends ShipAction
{
	/**
	 * The damage is being done by the shooting Ship.
	 */
	private int aDamage;
	/**
	 * The Ship being shot.
	 */
	private final Ship aTargetShip;

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
	public ShootShip(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		final Prop target = state.getPropFromID(j.getIntAttribute("targetID"));
		if (!(target instanceof Ship)) {
			throw new IllegalEVActionException("Target for shooting is not a ship");
		}
		aTargetShip = (Ship) target;
		aDamage = j.getIntAttribute("damage");
	}

	/**
	 * Create a new ShootShip action.
	 * 
	 * @param aggressor
	 *            The Ship doing the shooting.
	 * @param target
	 *            The Ship being shot.
	 * @param damage
	 *            The damage being done.
	 * @throws IllegalEVActionException
	 *             If the action is not valid.
	 */
	public ShootShip(final Ship aggressor, final Ship target, final int damage) throws IllegalEVActionException
	{
		super(aggressor);
		aTargetShip = target;
		aDamage = damage;
		if (damage > aggressor.getMaxDamage()) {
			throw new IllegalEVActionException("The shooting Ship cannot deal this much damage.");
		}
		// TODO - shooting distance
	}

	@Override
	protected void executeAction()
	{
		getShip().shoot(aTargetShip);
		// Remove health from the appropriate ship
		aTargetShip.takeDamage(aDamage);
	}

	@Override
	public String getDescription()
	{
		return "Shooting an enemy " + aTargetShip.getShipType();
	}

	/**
	 * @return The Ship being shot.
	 */
	public Ship getTarget()
	{
		return aTargetShip;
	}

	@Override
	protected boolean isValidShipAction()
	{
		// Conditions need to be met:
		// 1. Ship able to shoot target
		// 2. Ships within the same container, and that container has to be a solar system
		// 3. Target ship player is not the same as aggressor player
		// 4. Damage is feasible (Note: Client-side damage is set to a bogus value of -1, since damage is determined on server
		// side for obvious reasons)
		final Ship ship = getShip();
		return ship.canShoot(aTargetShip) && ship.getContainer().equals(aTargetShip.getContainer())
				&& (ship.getContainer() instanceof SolarSystem) && !getShip().getPlayer().equals(aTargetShip.getPlayer())
				&& aDamage <= ship.getMaxDamage();
	}

	/**
	 * Dead ships can shoot, because combat is done at the beginning of the turn and shooting happens before anything can
	 * visually die.
	 */
	@Override
	protected boolean requireShipAlive()
	{
		return false;
	}

	/**
	 * Roll the damage of this shot randomly; executed server-side. Damage is random between 80% to 100% of the ship's max
	 * damage output
	 */
	public void rollDamage()
	{
		aDamage = MathUtils.getRandomIntBetween(getShip().getMaxDamage() * 0.8f, getShip().getMaxDamage());
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("targetID", aTargetShip.getID());
		j.setAttribute("damage", aDamage);
		return j;
	}
}

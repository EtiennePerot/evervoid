package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.evervoid.utils.MathUtils;

public class ShootShip extends ShipAction
{
	private int aDamage;
	private final Ship aTargetShip;

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

	public ShootShip(final Ship aggressor, final Ship target, final int damage, final EVGameState state)
			throws IllegalEVActionException
	{
		super(aggressor, state);
		aTargetShip = target;
		aDamage = damage;
	}

	@Override
	protected void executeAction()
	{
		getShip().shoot(aTargetShip);
		// Remove health from the appropriate ship
		aTargetShip.takeDamage(aDamage);
	}

	public Ship getTarget()
	{
		return aTargetShip;
	}

	@Override
	protected boolean isValidShipAction()
	{
		// Conditions need to be met:
		// 1. Ship must be alive
		// 2. Ship must be able to shoot
		// 3. Ships within the same container, and that container has to be a solar system
		// 4. Target within distance
		// 5. Target ship player is not the same as aggressor player (FIXME: temporarily disabled)
		// 6. Damage is feasible (Note: Client-side damage is set to a bogus value of -1, since damage is determined on server
		// side for obvious reasons)
		final Ship ship = getShip();
		return !getShip().isDead() && ship.canShoot() && ship.getContainer().equals(aTargetShip.getContainer())
				&& (ship.getContainer() instanceof SolarSystem) && (ship.distanceTo(aTargetShip) < ship.getSpeed())
				&& aDamage <= ship.getMaxDamage();// && !aShip.getPlayer().equals(aTargetShip.getPlayer());
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
		j.setIntAttribute("targetID", aTargetShip.getID());
		j.setIntAttribute("damage", aDamage);
		return j;
	}
}

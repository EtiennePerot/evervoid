package com.evervoid.state.action.ship;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;

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

	public ShootShip(final Ship aggressor, final Ship target, final int damage) throws IllegalEVActionException
	{
		super("ShootShip", aggressor);
		aTargetShip = target;
		aDamage = damage;
	}

	@Override
	public void execute()
	{
		// Remove health from the appropriate ship
		aShip.shoot(aTargetShip);
		aTargetShip.loseHealth(aDamage);
	}

	@Override
	public boolean isValid()
	{
		// Conditions need to be met:
		// 1. Ship must be able to shoot
		// 2. Ships within the same container, and that container has to be a solar system
		// 3. Target within distance
		// 4. Target ship player is not the same as aggressor player (temporarily disabled)
		// 6. Damage is feasible (Note: Client-side damage is set to a bogus value of -1, since damage is determined on server
		// side for obvious reasons)
		return aShip.canShoot() && aShip.getContainer().equals(aTargetShip.getContainer())
				&& (aShip.getContainer() instanceof SolarSystem) && (aShip.distanceTo(aTargetShip) < aShip.getSpeed())
				&& aDamage <= aShip.getMaxDamage();// && !aShip.getPlayer().equals(aTargetShip.getPlayer());
	}

	/**
	 * Roll the damage of this shot randomly; executed server-side. Damage is random between 80% to 100% of the ship's max
	 * damage output
	 */
	public void rollDamage()
	{
		aDamage = MathUtils.getRandomIntBetween(aShip.getMaxDamage() * 0.8f, aShip.getMaxDamage());
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

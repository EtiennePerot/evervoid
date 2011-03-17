package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Ship;

public class ShootShip extends ShipAction
{
	private final int aDamage;
	private final Ship targetShip;

	public ShootShip(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		targetShip = (Ship) state.getPropFromID(j.getIntAttribute("targetID"));
		aDamage = j.getIntAttribute("dmg");
	}

	public ShootShip(final Ship agressor, final Ship target, final int damage) throws IllegalEVActionException
	{
		super("ShootShip", agressor);
		targetShip = target;
		aDamage = damage;
	}

	@Override
	public void execute()
	{
		// remove health from the appropirate ship
		aShip.shoot(targetShip);
		targetShip.loseHealth(aDamage);
	}

	@Override
	public boolean isValid()
	{
		// max shooting distance is currently based on speed. Might not want this.
		// four conditions need to be met:
		// 1. ships within the same container (TODO- enforce solar system?)
		// 2. target within distance
		// 3. damage is feasible number
		// 4. ship is not ours
		return aShip.getContainer().equals(targetShip.getContainer()) && (aShip.distanceTo(targetShip) < aShip.getSpeed())
				&& aDamage < aShip.getMaxDamage() && !aShip.getPlayer().equals(targetShip.getPlayer());
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("targetID", targetShip.getID());
		return j;
	}
}

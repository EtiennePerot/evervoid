package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Action;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Ship;

public abstract class ShipAction extends Action
{
	protected final Ship aShip;

	public ShipAction(final Json j, final EVGameState state)
	{
		super(j, state);
		// TODO - typecheck, throw error if not Ship instance
		aShip = (Ship) state.getPropFromID(j.getIntAttribute("shipID"));
	}

	public ShipAction(final Player player, final String actionType, final Ship ship)
	{
		super(player, "Ship" + actionType);
		aShip = ship;
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("shipID", aShip.getID());
		return j;
	}
}

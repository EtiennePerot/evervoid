package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Action;
import com.evervoid.state.prop.Ship;

public abstract class ShipAction extends Action
{
	protected final Ship aShip;

	public ShipAction(final Json j, final EVGameState state)
	{
		super(j, state);
		// TODO - typecheck, throw error if not Ship instance
		aShip = (Ship) aState.getPropFromID(j.getIntAttribute("shipID"));
	}

	@Override
	public Json toJson()
	{
		return super.toJson();
	}
}

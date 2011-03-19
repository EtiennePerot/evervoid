package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Action;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;

public abstract class ShipAction extends Action
{
	protected final Ship aShip;

	public ShipAction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		final Prop s = state.getPropFromID(j.getIntAttribute("shipid"));
		if (!(s instanceof Ship)) {
			throw new IllegalEVActionException("Prop is not a ship");
		}
		aShip = (Ship) s;
	}

	public ShipAction(final String actionType, final Ship ship) throws IllegalEVActionException
	{
		super(ship.getPlayer(), actionType);
		aShip = ship;
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("shipid", aShip.getID());
		return j;
	}
}

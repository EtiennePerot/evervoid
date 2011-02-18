package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.prop.Ship;

public class MoveShip extends ShipAction
{
	private final GridLocation aDestination;
	private final int aShipID;
	private final EVGameState aState;

	public MoveShip(final Json j, final EVGameState state)
	{
		super(j, state);
		aState = state;
		aDestination = new GridLocation(j.getAttribute("destination"));
		aShipID = j.getIntAttribute("shipID");
	}

	@Override
	public void execute()
	{
		final Ship ship = (Ship) aState.getPropFromID(aShipID);
		ship.moveTo(aDestination);
	}

	@Override
	public boolean isValid()
	{
		// TODO Auto-generated method stub
		return false;
	}
}

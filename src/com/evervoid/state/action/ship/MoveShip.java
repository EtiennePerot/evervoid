package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.geometry.GridLocation;

public class MoveShip extends ShipAction
{
	private final GridLocation aDestination;
	private final EVGameState aState;

	// private final int aShipID;
	public MoveShip(final Json j, final EVGameState state)
	{
		super(j, state);
		aState = state;
		aDestination = new GridLocation(j.getAttribute("destination"));
	}

	@Override
	protected void execute()
	{
		// aState.getShip(aShipID);
	}

	@Override
	public boolean isValid()
	{
		// TODO Auto-generated method stub
		return false;
	}
}

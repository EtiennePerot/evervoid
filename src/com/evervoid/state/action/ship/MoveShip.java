package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.geometry.GridLocation;

public class MoveShip extends ShipAction
{
	private final GridLocation aDestination;

	public MoveShip(final Json j, final EVGameState state)
	{
		super(j, state);
		aDestination = new GridLocation(j.getAttribute("destination"));
	}

	@Override
	public void execute()
	{
		aShip.move(aDestination);
	}

	@Override
	public boolean isValid()
	{
		final SolarSystem ss = (SolarSystem) aShip.getContainer();
		return !ss.isOccupied(aDestination);
	}
}

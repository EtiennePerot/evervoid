package com.evervoid.state.action.planet;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Planet;

public class IncrementShipConstruction extends PlanetAction
{
	private final String aShipType;

	public IncrementShipConstruction(final EVGameState state, final Planet planet, final String shipType)
			throws IllegalEVActionException
	{
		super(planet.getPlayer(), planet, state);
		aShipType = shipType;
		aPlayer.getRaceData().getShipData(shipType);
	}

	public IncrementShipConstruction(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aShipType = j.getStringAttribute("shipType");
	}

	@Override
	public void execute()
	{
		// all this does is decrease the build count, it is your job to actually build the ship when count == 0
		// the reason it is not done here is that it requires a game state, and this action does not contain one (nor should it)
		if (getPlanet().getShipProgress(aShipType) == -1) {
			// no progress, start building, debit cost
			getPlanet().startBuildingShip(aPlayer.getRaceData().getShipData(aShipType));
			aPlayer.getResources().remove(aPlayer.getRaceData().getShipData(aShipType).getCost());
		}
		else {
			getPlanet().continueBuildingShip();
		}
	}

	@Override
	protected boolean isValidPlanetAction()
	{
		return (getPlanet().getShipProgress(aShipType) == -1)
				|| aPlayer.hasResources(aPlayer.getRaceData().getShipData(aShipType).getCost());
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setStringAttribute("shipType", aShipType);
		return j;
	}
}

package com.evervoid.state.action.planet;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.prop.Planet;

public class RegeneratePlanet extends PlanetAction
{
	private final int aHealthAmount;
	private final int aShieldsAmount;

	public RegeneratePlanet(final Json j, final EVGameState state) throws IllegalEVActionException
	{
		super(j, state);
		aHealthAmount = j.getIntAttribute("health");
		aShieldsAmount = j.getIntAttribute("shields");
	}

	public RegeneratePlanet(final Planet planet, final EVGameState state) throws IllegalEVActionException
	{
		super(planet, state);
		aHealthAmount = planet.getHealthRegenRate();
		aShieldsAmount = planet.getShieldRegenRate();
	}

	@Override
	protected void executeAction()
	{
		getPlanet().addHealth(aHealthAmount);
		getPlanet().addShields(aShieldsAmount);
	}

	@Override
	public String getDescription()
	{
		return "regenerating " + getPlanet().getPlanetType() + " resources";
	}

	@Override
	protected boolean isValidPlanetAction()
	{
		// add health caps accordingly, not sure what needs to be checked here
		return true;
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("health", aHealthAmount);
		j.setIntAttribute("shields", aShieldsAmount);
		return j;
	}
}

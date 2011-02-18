package com.evervoid.state.action.planet;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Action;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;

public abstract class PlanetAction extends Action
{
	protected final Planet aPlanet;

	public PlanetAction(final Json j, final EVGameState state)
	{
		super(j, state);
		// TODO check typecast
		aPlanet = (Planet) state.getPropFromID(j.getIntAttribute("planetID"));
	}

	public PlanetAction(final Player player, final String actionType, final Planet planet)
	{
		super(player, "Planet" + actionType);
		aPlanet = planet;
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setIntAttribute("planetID", aPlanet.getID());
		return j;
	}
}

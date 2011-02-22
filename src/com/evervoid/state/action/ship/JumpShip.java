package com.evervoid.state.action.ship;

import com.evervoid.json.Json;
import com.evervoid.state.EVContainer;
import com.evervoid.state.EVGameState;
import com.evervoid.state.Wormhole;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;

public class JumpShip extends ShipAction
{
	final EVContainer<Prop> aDestination;

	public JumpShip(final Json j, final EVGameState state)
	{
		super(j, state);
		final String destinationType = j.getStringAttribute("destinationType");
		if (destinationType.equals("wormhole")) {
			aDestination = state.getWormhole(j.getIntAttribute("wid"));
		}
		else {
			aDestination = state.getSolarSystem(j.getIntAttribute("ssid"));
		}
	}

	public JumpShip(final Player player, final Ship ship, final EVContainer<Prop> destination)
	{
		super(player, "JumpShip", ship);
		aDestination = destination;
	}

	@Override
	public void execute()
	{
		aShip.jump(aDestination);
	}

	@Override
	public boolean isValid()
	{
		// TODO - check that the new container is connected to the current
		return true;
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		if (aDestination instanceof Wormhole) {
			j.setStringAttribute("destinationType", "wormhole");
			j.setIntAttribute("wid", aDestination.getID());
		}
		else {
			j.setStringAttribute("destinationType", "solarsystem");
			j.setIntAttribute("ssid", aDestination.getID());
		}
		return j;
	}
}

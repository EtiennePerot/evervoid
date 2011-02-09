package com.evervoid.state.prop;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.GridLocation;
import com.evervoid.state.player.Player;

public abstract class Prop implements Jsonable
{
	protected GridLocation aLocation;
	protected final Player aPlayer;

	protected Prop(final Player player, final GridLocation location)
	{
		aPlayer = player;
		aLocation = location;
	}

	public GridLocation getLocation()
	{
		return aLocation;
	}

	void move(final GridLocation location)
	{
		aLocation = location;
	}

	@Override
	public Json toJson()
	{
		return new Json().setStringAttribute("player", aPlayer.getName()).setAttribute("location", aLocation)
				.setStringAttribute("proptype", "unknown");
	}
}

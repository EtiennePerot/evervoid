package com.evervoid.state.prop;

import com.evervoid.json.Json;
import com.evervoid.state.GridLocation;
import com.evervoid.state.player.Player;

public class Star extends Prop
{
	public Star(final Player player, final GridLocation location)
	{
		super(player, location);
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("location", aLocation);
	}
}

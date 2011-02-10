package com.evervoid.state.prop;

import com.evervoid.json.Json;
import com.evervoid.state.EverVoidGameState;
import com.evervoid.state.GridLocation;

public class Star extends Prop
{
	public static Star fromJson(final Json j, final EverVoidGameState state)
	{
		return new Star(GridLocation.fromJson(j.getAttribute("location")), state);
	}

	public Star(final GridLocation location, final EverVoidGameState state)
	{
		super(state.getNullPlayer(), location);
	}

	@Override
	public Json toJson()
	{
		return super.toJson().setStringAttribute("proptype", "star");
	}
}

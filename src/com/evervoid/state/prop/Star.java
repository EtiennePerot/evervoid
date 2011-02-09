package com.evervoid.state.prop;

import com.evervoid.json.Json;
import com.evervoid.state.GridLocation;

public class Star extends Prop
{
	public static Star fromJson(final Json j)
	{
		return new Star(GridLocation.fromJson(j.getAttribute("location")));
	}

	public Star(final GridLocation location)
	{
		super(null, location);
	}

	@Override
	public Json toJson()
	{
		return super.toJson().setStringAttribute("proptype", "star");
	}
}

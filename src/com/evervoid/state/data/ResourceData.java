package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class ResourceData implements Jsonable
{
	private final String aName;
	private final int aTurnRate;

	public ResourceData(final Json j)
	{
		aName = j.getStringAttribute("name");
		aTurnRate = j.getIntAttribute("rate");
	}

	public ResourceData(final String name, final int rate)
	{
		aName = name;
		aTurnRate = rate;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setStringAttribute("name", aName);
		j.setIntAttribute("rate", aTurnRate);
		return j;
	}
}

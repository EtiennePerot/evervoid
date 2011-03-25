package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class BuildingData implements Jsonable
{
	private final String aTitle;
	private final String aType;

	public BuildingData(final String type, final Json j)
	{
		aType = type;
		aTitle = j.getStringAttribute("title");
	}

	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setStringAttribute("title", aType);
		return j;
	}
}

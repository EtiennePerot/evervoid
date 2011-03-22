package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class ResourceData implements Jsonable
{
	private final String aName;
	private final int aStartValue;

	public ResourceData(final String name, final int rate, final int start)
	{
		aName = name;
		aStartValue = start;
	}

	public ResourceData(final String name, final Json j)
	{
		aName = name;
		aStartValue = j.getIntAttribute("start");
	}

	public String getName()
	{
		return aName;
	}

	public int getStartValue()
	{
		return aStartValue;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setIntAttribute("start", aStartValue);
		return j;
	}
}

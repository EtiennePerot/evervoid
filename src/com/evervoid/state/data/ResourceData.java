package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class ResourceData implements Jsonable
{
	private final String aName;
	private final int aStartValue;
	private final int aTurnRate;

	public ResourceData(final String name, final int rate, final int start)
	{
		aName = name;
		aTurnRate = rate;
		aStartValue = start;
	}

	public ResourceData(final String name, final Json j)
	{
		aName = name;
		aTurnRate = j.getIntAttribute("rate");
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

	public int getTurnRate()
	{
		return aTurnRate;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setIntAttribute("rate", aTurnRate);
		j.setIntAttribute("start", aStartValue);
		return j;
	}
}

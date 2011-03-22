package com.evervoid.state.player;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.data.ResourceData;

public class Resource implements Jsonable
{
	private int aCurrentAmount;
	private final String aResourceName;

	public Resource(final Json j)
	{
		aResourceName = j.getStringAttribute("name");
		aCurrentAmount = j.getIntAttribute("amount");
	}

	public Resource(final ResourceData r)
	{
		aResourceName = r.getName();
		aCurrentAmount = r.getStartValue();
	}

	public int add(final int add)
	{
		aCurrentAmount += add;
		return aCurrentAmount;
	}

	public int getCurrent()
	{
		return aCurrentAmount;
	}

	public int remove(final int remove)
	{
		if (remove <= aCurrentAmount) {
			aCurrentAmount -= remove;
			return aCurrentAmount;
		}
		return -1;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setStringAttribute("name", aResourceName);
		j.setIntAttribute("amount", aCurrentAmount);
		return j;
	}
}

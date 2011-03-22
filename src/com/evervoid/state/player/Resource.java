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

	public Resource(final ResourceData data)
	{
		aResourceName = data.getName();
		aCurrentAmount = data.getStartValue();
	}

	public int add(final int amount)
	{
		aCurrentAmount = Math.max(0, aCurrentAmount + amount);
		return aCurrentAmount;
	}

	public int getCurrent()
	{
		return aCurrentAmount;
	}

	public int remove(final int amount)
	{
		return add(-amount);
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

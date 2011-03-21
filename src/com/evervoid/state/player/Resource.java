package com.evervoid.state.player;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.data.ResourceData;

public class Resource implements Jsonable
{
	private final String aName;
	private int current;

	public Resource(final Json j)
	{
		aName = j.getStringAttribute("name");
	}

	public Resource(final ResourceData r)
	{
		aName = r.getName();
		current = r.getStartValue();
	}

	public int add(final int add)
	{
		current += add;
		return current;
	}

	public int getCurrent()
	{
		return current;
	}

	public int remove(final int remove)
	{
		if (remove <= current) {
			current -= remove;
			return current;
		}
		return -1;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setStringAttribute("name", aName);
		j.setIntAttribute("curr", current);
		return j;
	}
}

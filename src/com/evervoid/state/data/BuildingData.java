package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.player.ResourceAmount;

public class BuildingData implements Jsonable
{
	private final int aBuildTime;
	private final ResourceAmount aCost;
	private final String aTitle;
	private final String aType;

	public BuildingData(final String type, final Json j)
	{
		aType = type;
		aTitle = j.getStringAttribute("title");
		aCost = new ResourceAmount(j.getAttribute("cost"));
		aBuildTime = j.getIntAttribute("buildTime");
	}

	public String getTitle()
	{
		return aTitle;
	}

	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setStringAttribute("title", aTitle);
		j.setAttribute("cost", aCost);
		j.setIntAttribute("buildTime", aBuildTime);
		return j;
	}
}

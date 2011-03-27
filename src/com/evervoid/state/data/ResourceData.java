package com.evervoid.state.data;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class ResourceData implements Jsonable
{
	private final String aTitle;
	private final String aType;

	public ResourceData(final String type, final Json j)
	{
		aType = type;
		aTitle = j.getStringAttribute("title");
	}

	public String getIcon()
	{
		return "icons/resources/" + aType + ".png";
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
		return new Json().setStringAttribute("title", aTitle);
	}
}

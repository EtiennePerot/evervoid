package com.evervoid.client.settings;

import java.util.List;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class ClientSettings implements Jsonable
{
	private String aNickname;

	public ClientSettings()
	{
		// Check if file in home/appdata directory
		// If not, copy default preferences from schema
		// Load file in home/appdata
		// FIXME: Temporary nickname random generation
		final List<Json> names = Json.fromFile("res/schema/players.json").getListAttribute("names");
		aNickname = ((Json) MathUtils.getRandomElement(names)).getString();
	}

	public void close()
	{
		// Write back to file
	}

	public String getNickname()
	{
		return aNickname;
	}

	public void setNickname(final String text)
	{
		aNickname = text;
	}

	@Override
	public Json toJson()
	{
		// TODO Auto-generated method stub
		return null;
	}
}

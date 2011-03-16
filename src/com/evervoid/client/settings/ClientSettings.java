package com.evervoid.client.settings;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Color;

public class ClientSettings implements Jsonable
{
	private static final String filename = "res/home/appdata/preferences.json";
	private Color aColor;
	private String aNickname;

	public ClientSettings()
	{
		// Check if file in home/appdata directory
		// If not, copy default preferences from schema
		// Load file in home/appdata
		// FIXME: Temporary nickname random generation
		aColor = Color.random();
		if (!loadSettings()) {
			final List<Json> names = Json.fromFile("res/schema/players.json").getListAttribute("names");
			aNickname = ((Json) MathUtils.getRandomElement(names)).getString();
		}
	}

	public ClientSettings(final Json j)
	{
		aNickname = j.getStringAttribute("name");
		aColor = new Color(j.getAttribute("color"));
	}

	public void close()
	{
		// Write back to file
	}

	public String getNickname()
	{
		return aNickname;
	}

	public boolean loadSettings()
	{
		final Json j = Json.fromFile(filename);
		try {
			aNickname = j.getStringAttribute("name");
			aColor = new Color(j.getAttribute("color"));
		}
		catch (final Exception e) {
			Logger.getLogger(EverVoidClient.class.getName()).warning("Settings File did not exist");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void setNickname(final String text)
	{
		aNickname = text;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setStringAttribute("name", aNickname);
		j.setAttribute("color", aColor);
		return j;
	}

	@Override
	public String toString()
	{
		return toJson().toPrettyString();
	}

	public void writeSettings()
	{
		final File file = new File(filename);
		try {
			file.createNewFile();
		}
		catch (final IOException e) {
			// file already exists
		}
		toJson().toFile(file.getPath());
	}
}

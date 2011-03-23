package com.evervoid.client.settings;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class ClientSettings implements Jsonable
{
	private String aNickname;
	private final String filename;

	public ClientSettings()
	{
		// detect OS in oder to save to correct location
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			// windows
			filename = System.getenv("APPDATA") + "/application.json";
		}
		else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			// mac
			filename = System.getProperty("user.home") + "/Library/Application Support/everVoid/preferences.json";
		}
		else {
			// default - assume unix
			filename = System.getProperty("user.home") + "/.everVoid/preferences.json";
		}
		Json j = null;
		if ((new File(filename).exists())) {
			j = Json.fromFile(filename);
		}
		else {
			EverVoidClient.getLogger().log(
					Level.INFO,
					"Local preference file does not exist (on operating system " + System.getProperty("os.name").toLowerCase()
							+ ")");
			j = Json.fromFile("res/home/appdata/preferences.json");
		}
		// name loading
		if (j.getAttribute("name").isNullNode()) {
			// load random name
			final List<Json> names = Json.fromFile("res/schema/players.json").getListAttribute("names");
			aNickname = ((Json) MathUtils.getRandomElement(names)).getString();
		}
		else {
			aNickname = j.getStringAttribute("name");
		}
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
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		catch (final IOException e) {
			// file already exists
		}
		toJson().toFile(file.getPath());
	}
}

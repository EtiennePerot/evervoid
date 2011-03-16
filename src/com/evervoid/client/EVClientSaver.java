package com.evervoid.client;

import java.io.File;
import java.io.FileOutputStream;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;

public class EVClientSaver
{
	public static final String saveLocation = "";
	private static EVClientSaver sInstance;

	static EVClientSaver getInstance()
	{
		if (sInstance == null) {
			sInstance = new EVClientSaver();
		}
		return sInstance;
	}

	private EVClientSaver()
	{
		sInstance = this;
	}

	public Json loadGame(final String filename)
	{
		return Json.fromFile(filename);
	}

	public boolean saveGame(final String name, final EVGameState state)
	{
		final String stateString = state.toJson().toPrettyString();
		try {
			final FileOutputStream fileStream = new FileOutputStream(name.replace("/", File.separator));
			fileStream.write(stateString.getBytes());
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}

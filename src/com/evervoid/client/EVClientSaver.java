package com.evervoid.client;

import java.io.File;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;

public class EVClientSaver
{
	private static EVClientSaver sInstance = null;

	private static EVClientSaver getInstance()
	{
		if (sInstance == null) {
			sInstance = new EVClientSaver();
		}
		return sInstance;
	}

	public static void save(final String filename, final EVGameState state)
	{
		getInstance().saveGame(filename, state);
	}

	private EVClientSaver()
	{
	}

	public Json loadGame(final String filename)
	{
		return Json.fromFile(new File(EverVoidClient.getSettings().getAppData(), filename));
	}

	public boolean saveGame(final String name, final EVGameState state)
	{
		return state.toJson().toFile(new File(EverVoidClient.getSettings().getAppData(), name));
	}
}

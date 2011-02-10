package com.evervoid.state.player;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class Research implements Jsonable
{
	public static Research fromJson(final Json j)
	{
		// TODO: Do something
		return new Research();
	}

	@Override
	public Json toJson()
	{
		// TODO: Do something
		return new Json();
	}
}

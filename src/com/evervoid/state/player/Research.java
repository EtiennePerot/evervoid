package com.evervoid.state.player;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.observers.ResearchObserver;

public class Research implements Jsonable
{
	public static Research fromJson(final Json j)
	{
		// TODO: Do something
		return new Research();
	}

	private final Set<ResearchObserver> aObserverSet;

	public Research()
	{
		aObserverSet = new HashSet<ResearchObserver>();
	}

	public void deregisterObserver(final ResearchObserver rObserver)
	{
		aObserverSet.remove(rObserver);
	}

	public void registerObserver(final ResearchObserver rObserver)
	{
		aObserverSet.add(rObserver);
	}

	@Override
	public Json toJson()
	{
		// TODO: Do something
		return new Json();
	}
}

package com.evervoid.state.player;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.observers.ResearchObserver;

public class Research implements Jsonable
{
	/**
	 * @param j
	 *            The Json serialization of the Research object
	 * @return The Research represented by the passed Json.
	 */
	public static Research fromJson(final Json j)
	{
		// TODO: Do something
		return new Research();
	}

	/**
	 * The set of all objects currently observing this Research.
	 */
	private final Set<ResearchObserver> aObserverSet;

	/**
	 * Creates a new, empty Research.
	 */
	public Research()
	{
		aObserverSet = new HashSet<ResearchObserver>();
	}

	/**
	 * unregisters an observer from this Research
	 * 
	 * @param rObserver
	 *            the observer to unregister
	 */
	public void deregisterObserver(final ResearchObserver rObserver)
	{
		aObserverSet.remove(rObserver);
	}

	/**
	 * registers an observer to this Research
	 * 
	 * @param rObserver
	 *            the observer to register
	 */
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

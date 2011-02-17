package com.evervoid.state.prop;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.data.PlanetData;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.observers.PlanetObserver;
import com.evervoid.state.player.Player;

public class Planet extends Prop
{
	private final PlanetData aData;
	private final Set<PlanetObserver> aObserverSet;

	public Planet(final Json j, final EVGameState state)
	{
		super(j, state, "planet");
		aData = state.getPlanetData(j.getStringAttribute("planettype"));
		aObserverSet = new HashSet<PlanetObserver>();
	}

	public Planet(final Player player, final GridLocation location, final String type, final EVGameState state)
	{
		super(player, location, state, "planet");
		aData = state.getPlanetData(type);
		aLocation.dimension = aData.getDimension();
		aObserverSet = new HashSet<PlanetObserver>();
	}

	public void deregisterObserver(final PlanetObserver pObserver)
	{
		aObserverSet.remove(pObserver);
	}

	public PlanetData getData()
	{
		return aData;
	}

	public void registerObserver(final PlanetObserver pObserver)
	{
		aObserverSet.add(pObserver);
	}

	@Override
	public Json toJson()
	{
		return basePropJson().setStringAttribute("planettype", aData.getType());
	}
}

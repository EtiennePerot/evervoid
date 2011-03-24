package com.evervoid.state.prop;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.data.PlanetData;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.observers.PlanetObserver;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;
import com.evervoid.utils.Pair;

public class Planet extends Prop
{
	private final PlanetData aData;
	private final Set<PlanetObserver> aObserverSet;
	// Java doesn't have pairs, so screw you we're using Map Entries
	private Pair<ShipData, Integer> aShipProgress;

	public Planet(final int id, final Player player, final GridLocation location, final String type, final EVGameState state)
	{
		super(id, player, location, "planet", state);
		aData = state.getPlanetData(type);
		aLocation.dimension = aData.getDimension();
		aObserverSet = new HashSet<PlanetObserver>();
	}

	public Planet(final Json j, final Player player, final PlanetData data, final EVGameState state)
	{
		super(j, player, "planet", state);
		aData = data;
		aObserverSet = new HashSet<PlanetObserver>();
		final Json shipJson = j.getAttribute("ship");
		if (shipJson.isNullNode()) {
			aShipProgress = null;
		}
		else {
			aShipProgress = new Pair<ShipData, Integer>(aPlayer.getRaceData().getShipData(shipJson.getStringAttribute("name")),
					shipJson.getIntAttribute("progress"));
		}
	}

	public boolean continueBuildingShip()
	{
		if (aShipProgress == null) {
			return false;
		}
		aShipProgress.setValue(aShipProgress.getValue() - 1);
		if (aShipProgress.getValue() == 0) {
			aShipProgress = null;
			return true;
		}
		return false;
	}

	public void deregisterObserver(final PlanetObserver pObserver)
	{
		aObserverSet.remove(pObserver);
	}

	public PlanetData getData()
	{
		return aData;
	}

	public ResourceAmount getResourceRate()
	{
		return aData.getResourceRate();
	}

	public int getShipProgress(final String shipType)
	{
		return (aShipProgress == null) ? -1 : aShipProgress.getValue();
	}

	public void registerObserver(final PlanetObserver pObserver)
	{
		aObserverSet.add(pObserver);
	}

	public void startBuildingShip(final ShipData shipData)
	{
		aShipProgress = new Pair<ShipData, Integer>(shipData, shipData.getBuildingTime());
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setStringAttribute("planettype", aData.getType());
		if (aShipProgress == null) {
			j.setAttribute("ship", null);
		}
		else {
			j.setAttribute(
					"ship",
					new Json().setStringAttribute("name", aShipProgress.getKey().getType()).setIntAttribute("progress",
							aShipProgress.getValue()));
		}
		return j;
	}
}

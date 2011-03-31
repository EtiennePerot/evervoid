package com.evervoid.state.building;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVGameState;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;
import com.evervoid.utils.Pair;

public class Building implements Jsonable, Comparable<Building>
{
	private final BuildingData aData;
	private final int aID;
	private final Planet aPlanet;
	private Pair<ShipData, Integer> aShipProgress;
	private final EVGameState aState;

	public Building(final EVGameState state, final Planet planet, final BuildingData data)
	{
		aState = state;
		aPlanet = planet;
		aData = data;
		aID = state.getNextPlanetID();
		state.registerBuilding(this);
	}

	public Building(final Json j, final EVGameState state)
	{
		aState = state;
		aID = j.getIntAttribute("id");
		aPlanet = (Planet) state.getPropFromID(j.getIntAttribute("planet"));
		aData = state.getBuildingData(getPlayer().getRaceData().getType(), j.getStringAttribute("type"));
		final Json shipJson = j.getAttribute("ship");
		if (shipJson.isNull()) {
			aShipProgress = null;
		}
		else {
			aShipProgress = new Pair<ShipData, Integer>(getPlayer().getRaceData().getShipData(
					shipJson.getStringAttribute("name")), shipJson.getIntAttribute("progress"));
		}
		state.registerBuilding(this);
	}

	@Override
	public int compareTo(final Building other)
	{
		return getID() - other.getID();
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

	public void deregister()
	{
		aState.deregisterBuilding(getID());
	}

	public int getID()
	{
		return aID;
	}

	public Player getPlayer()
	{
		return aPlanet.getPlayer();
	}

	public Pair<ShipData, Integer> getShipProgress()
	{
		return aShipProgress;
	}

	public int getShipProgress(final String shipType)
	{
		return (aShipProgress == null) ? -1 : aShipProgress.getValue();
	}

	public String getType()
	{
		return aData.getType();
	}

	public void startBuildingShip(final ShipData shipData)
	{
		aShipProgress = new Pair<ShipData, Integer>(shipData, shipData.getBaseBuildTime());
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setIntAttribute("id", aID);
		j.setStringAttribute("player", getPlayer().getName());
		j.setIntAttribute("planet", aPlanet.getID());
		j.setStringAttribute("type", aData.getType());
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

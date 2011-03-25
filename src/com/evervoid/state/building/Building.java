package com.evervoid.state.building;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVGameState;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;
import com.evervoid.utils.Pair;

public class Building implements Jsonable
{
	final private BuildingData aData;
	final private int aID;
	final private Planet aPlanet;
	// Java doesn't have pairs, so screw you we're using Map Entries
	private Pair<ShipData, Integer> aShipProgress;
	final private EVGameState aState;

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
		if (shipJson.isNullNode()) {
			aShipProgress = null;
		}
		else {
			aShipProgress = new Pair<ShipData, Integer>(getPlayer().getRaceData().getShipData(
					shipJson.getStringAttribute("name")), shipJson.getIntAttribute("progress"));
		}
		state.registerBuilding(this);
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
		aState.deregisterBuilding(getId());
	}

	public int getId()
	{
		return aID;
	}

	public Player getPlayer()
	{
		return aPlanet.getPlayer();
	}

	public int getShipProgress(final String shipType)
	{
		return (aShipProgress == null) ? -1 : aShipProgress.getValue();
	}

	public void startBuildingShip(final ShipData shipData)
	{
		aShipProgress = new Pair<ShipData, Integer>(shipData, shipData.getBuildingTime());
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
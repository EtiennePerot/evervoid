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
	private int aBuildingProgress;
	private final BuildingData aData;
	private final int aID;
	private final Planet aPlanet;
	private Pair<ShipData, Integer> aShipProgress;
	private final EVGameState aState;

	public Building(final EVGameState state, final Planet planet, final BuildingData data, final boolean isBuilt)
	{
		aState = state;
		aPlanet = planet;
		aData = data;
		aID = state.getNextBuildingID();
		aBuildingProgress = isBuilt ? aData.getBuildTime() : 0;
		state.registerBuilding(this);
	}

	public Building(final Json j, final EVGameState state)
	{
		aState = state;
		aID = j.getIntAttribute("id");
		aPlanet = (Planet) state.getPropFromID(j.getIntAttribute("planet"));
		aData = state.getBuildingData(getPlayer().getRaceData().getType(), j.getStringAttribute("type"));
		aBuildingProgress = j.getIntAttribute("progress");
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

	public int getBuildingProgress()
	{
		return aBuildingProgress;
	}

	public String getBuildingProgressPercentage()
	{
		return String.valueOf((int) (aBuildingProgress * 100d / aData.getBuildTime())) + "%";
	}

	public BuildingData getData()
	{
		return aData;
	}

	public int getID()
	{
		return aID;
	}

	public Planet getPlanet()
	{
		return aPlanet;
	}

	public Player getPlayer()
	{
		return aPlanet.getPlayer();
	}

	public float getShipConstructionFloat()
	{
		if (!isBuildingShip()) {
			return 0;
		}
		return (float) getShipProgress().getValue() / (float) getShipCurrentlyBuilding().getBaseBuildTime();
	}

	public String getShipConstructionPercentage()
	{
		return (int) getShipConstructionFloat() + "%";
	}

	public ShipData getShipCurrentlyBuilding()
	{
		if (aShipProgress == null) {
			return null;
		}
		return aShipProgress.getKey();
	}

	public Pair<ShipData, Integer> getShipProgress()
	{
		return aShipProgress;
	}

	public int getSlot()
	{
		return aPlanet.getSlotForBuilding(this);
	}

	public String getType()
	{
		return aData.getType();
	}

	public boolean incrementBuildingProgress()
	{
		if (!isBuildingComplete()) {
			aBuildingProgress++;
		}
		return isBuildingComplete();
	}

	/**
	 * Starts or increment a ship's progress in this building
	 * 
	 * @param shipData
	 *            The ShipData of the ship to progress
	 * @return Whether construction is complete or not
	 */
	public boolean incrementShipProgress(final ShipData shipData)
	{
		if (shipData == null || !isBuildingComplete()) {
			return false;
		}
		if (aShipProgress == null || !aShipProgress.getKey().equals(shipData)) {
			// Build new ship
			aShipProgress = new Pair<ShipData, Integer>(shipData, 0);
		}
		final int maxProgress = shipData.getBaseBuildTime();
		aShipProgress.setValue(Math.min(maxProgress, aShipProgress.getValue() + 1));
		return aShipProgress.getValue() == maxProgress;
	}

	public boolean isBuildingComplete()
	{
		return aBuildingProgress >= aData.getBuildTime();
	}

	public boolean isBuildingShip()
	{
		return aShipProgress != null;
	}

	public void resetShipConstruction()
	{
		aShipProgress = null;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setIntAttribute("id", aID);
		j.setStringAttribute("player", getPlayer().getName());
		j.setIntAttribute("planet", aPlanet.getID());
		j.setStringAttribute("type", aData.getType());
		j.setIntAttribute("progress", aBuildingProgress);
		if (aShipProgress == null) {
			j.setAttribute("ship", null);
		}
		else {
			// Perfect example of autoformatter failing:
			j.setAttribute(
					"ship",
					new Json().setStringAttribute("name", aShipProgress.getKey().getType()).setIntAttribute("progress",
							aShipProgress.getValue()));
		}
		return j;
	}
}

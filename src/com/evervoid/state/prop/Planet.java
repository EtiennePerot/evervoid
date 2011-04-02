package com.evervoid.state.prop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.building.Building;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.data.PlanetData;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.observers.PlanetObserver;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;

public class Planet extends Prop
{
	private final Map<Integer, Building> aBuildings = new HashMap<Integer, Building>();
	private int aCurrentHealth;
	private final PlanetData aData;
	private final Set<PlanetObserver> aObserverSet;

	public Planet(final int id, final Player player, final GridLocation location, final String type, final EVGameState state)
	{
		super(id, player, location, "planet", state);
		aData = state.getPlanetData(type);
		aLocation.dimension = aData.getDimension();
		aObserverSet = new HashSet<PlanetObserver>();
		// Initialize map to all-null buildings
		for (int b = 0; b < aData.getNumOfBuildingSlots(); b++) {
			aBuildings.put(b, null);
		}
		aCurrentHealth = aData.getBaseHealth();
	}

	public Planet(final Json j, final PlanetData data, final EVGameState state)
	{
		super(j, "planet", state);
		aData = data;
		aObserverSet = new HashSet<PlanetObserver>();
		aState.registerProp(this, aContainer);
		aCurrentHealth = j.getIntAttribute("health");
		final Json buildingsJson = j.getAttribute("buildings");
		String slot;
		for (int b = 0; b < aData.getNumOfBuildingSlots(); b++) {
			slot = String.valueOf(b);
			if (buildingsJson.hasAttribute(slot) && !buildingsJson.getAttribute(slot).isNull()) {
				aBuildings.put(b, new Building(buildingsJson.getAttribute(slot), state));
			}
			else {
				aBuildings.put(b, null);
			}
		}
	}

	public void bomb(final int damage)
	{
		// TODO shields
		aCurrentHealth -= damage;
		if (aCurrentHealth <= 0) {
			// planet destroyed, go neutral
			changeOwner(aState.getNullPlayer());
		}
	}

	public void changeOwner(final Player player)
	{
		aPlayer = player;
		for (final PlanetObserver observer : aObserverSet) {
			observer.planetCaptured(this, aPlayer);
		}
	}

	public void deleteBuildings()
	{
		for (final Building b : aBuildings.values()) {
			b.deregister();
		}
		aBuildings.clear();
	}

	public void deregisterObserver(final PlanetObserver pObserver)
	{
		aObserverSet.remove(pObserver);
	}

	public Building getBuildingAt(final int slot)
	{
		if (!hasSlot(slot)) {
			return null;
		}
		return aBuildings.get(slot);
	}

	public Integer getBuildingProgress(final int slot)
	{
		if (!hasSlot(slot)) {
			return null;
		}
		return aBuildings.get(slot).getBuildingProgress();
	}

	public Map<Integer, Building> getBuildings()
	{
		return aBuildings;
	}

	public int getCurrentHealth()
	{
		return aCurrentHealth;
	}

	public PlanetData getData()
	{
		return aData;
	}

	public int getMaxHealth()
	{
		// TODO deal with research
		return aData.getBaseHealth();
	}

	public String getPlanetType()
	{
		return aData.getTitle();
	}

	public ResourceAmount getResourceRate()
	{
		return aData.getResourceRate();
	}

	public boolean hasSlot(final int slot)
	{
		return slot >= 0 && slot < aData.getNumOfBuildingSlots();
	}

	/**
	 * Starts or increments a building construction
	 * 
	 * @param slot
	 *            The building slot
	 * @param building
	 *            The type of building to build
	 */
	public void incrementBuilding(final int slot, final BuildingData building)
	{
		if (!hasSlot(slot) || isBuildingComplete(slot)) {
			// Invalid slot or building already in place
			return;
		}
		Building b = getBuildingAt(slot);
		if (b == null || !b.getData().equals(building)) {
			// There was no building in construction there, or there was another type of building in construction
			if (b != null) {
				b.deregister();
			}
			b = new Building(getState(), this, building, false);
			aBuildings.put(slot, b);
		}
		b.incrementProgress();
		for (final PlanetObserver obs : aObserverSet) {
			obs.buildingsChanged(this);
		}
	}

	public boolean isBuildingComplete(final int slot)
	{
		return getBuildingAt(slot) != null && getBuildingAt(slot).isComplete();
	}

	public boolean isSlotFree(final int slot)
	{
		return hasSlot(slot) && aBuildings.get(slot) == null;
	}

	public void registerObserver(final PlanetObserver pObserver)
	{
		aObserverSet.add(pObserver);
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setStringAttribute("planettype", aData.getType());
		j.setIntMapAttribute("buildings", aBuildings);
		j.setIntAttribute("health", aCurrentHealth);
		return j;
	}
}

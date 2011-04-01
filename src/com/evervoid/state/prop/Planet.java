package com.evervoid.state.prop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.building.Building;
import com.evervoid.state.data.PlanetData;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.observers.PlanetObserver;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;

public class Planet extends Prop
{
	private final Map<Integer, Building> aBuildings;
	private int aCurrentHealth;
	private final PlanetData aData;
	private final Set<PlanetObserver> aObserverSet;

	public Planet(final int id, final Player player, final GridLocation location, final String type, final EVGameState state)
	{
		super(id, player, location, "planet", state);
		aData = state.getPlanetData(type);
		aLocation.dimension = aData.getDimension();
		aObserverSet = new HashSet<PlanetObserver>();
		aBuildings = new HashMap<Integer, Building>();
		// Initialize map to all-null buildings
		for (int b = 0; b < aData.getNumOfBuildingSlots(); b++) {
			aBuildings.put(b, null);
		}
		// FIXME adding a default building in each planet to test
		aBuildings.put(0,
				new Building(aState, this, aPlayer.getRaceData().getBuildingData(aPlayer.getBuildings().iterator().next())));
		// FIXME get from Data
		aCurrentHealth = aData.getBaseHealth();
	}

	public Planet(final Json j, final PlanetData data, final EVGameState state)
	{
		super(j, "planet", state);
		aData = data;
		aObserverSet = new HashSet<PlanetObserver>();
		aState.registerProp(this, aContainer);
		aBuildings = new HashMap<Integer, Building>();
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

	public void addBuilding(final int buildingSlot, final Building building)
	{
		// TODO - check if the planet can build it
		// check that there is enough room to build
		aBuildings.put(buildingSlot, building);
	}

	public void bomb(final int damage)
	{
		// TODO shileds
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
			observer.planetChangedOwner(this);
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
		return aBuildings.get(slot);
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

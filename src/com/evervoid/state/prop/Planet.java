package com.evervoid.state.prop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;
import com.evervoid.state.building.Building;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.data.PlanetData;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.observers.PlanetObserver;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;
import com.evervoid.utils.MathUtils;

public class Planet extends Prop
{
	/**
	 * A map of all Buildings currently constructed on this Planet.
	 */
	private final Map<Integer, Building> aBuildings = new HashMap<Integer, Building>();
	/**
	 * The current number of health points of this Planet.
	 */
	private int aCurrentHealth;
	/**
	 * The number of Shields this planet currently has.
	 */
	private int aCurrentShields;
	/**
	 * This Planet's Data.
	 */
	private final PlanetData aData;
	/**
	 * The set of all objects currently observing this Planet.
	 */
	private final Set<PlanetObserver> aObserverSet;

	/**
	 * Creates a Planet with the parameters passed.
	 * 
	 * @param id
	 *            The id associated with this Planet in the state.
	 * @param player
	 *            The Planet's original owner.
	 * @param location
	 *            The planet's location within the SolarSystem.
	 * @param type
	 *            The String representation of this Planet's type.
	 * @param state
	 *            The state this Planet belongs to.
	 */
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

	/**
	 * Creates a Planet from the contents of the parameter Json.
	 * 
	 * @param j
	 *            The Json containing all information necessary to build the Planet.
	 * @param state
	 *            The State this Planet will belong to.
	 */
	public Planet(final Json j, final EVGameState state)
	{
		super(j, state);
		aData = state.getPlanetData(j.getStringAttribute("planettype"));
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

	/**
	 * Adds a building at the appropriate slot if that is a legal action.
	 * 
	 * @param slot
	 *            The slot at which the Planet should be added.
	 * @param building
	 *            The building to add.
	 */
	public void addBuilding(final int slot, final Building building)
	{
		aBuildings.put(slot, building);
	}

	/**
	 * Adds the given amount to this Planet's health; negative number subtract. The result is bounded between zero and max
	 * health, as determined by the current research.
	 * 
	 * @param amount
	 *            The amount of health to add.
	 */
	public void addHealth(final int amount)
	{
		final int prevHealth = aCurrentHealth;
		aCurrentHealth = MathUtils.clampInt(0, aCurrentHealth + amount, getMaxHealth());
		for (final PlanetObserver obs : aObserverSet) {
			obs.healthChanged(this, aCurrentHealth - prevHealth);
		}
		if (aCurrentHealth == 0) {
			// planet destroyed, make it neutral
			changeOwner(aState.getNullPlayer());
		}
	}

	/**
	 * Adds the given amount to this Planet's shields; negative number subtract. The result is bounded between zero and max
	 * shields as determined by the current research level.
	 * 
	 * @param amount
	 *            The amount to add.
	 */
	public void addShields(final int amount)
	{
		final int previousShields = aCurrentShields;
		aCurrentShields = MathUtils.clampInt(0, aCurrentShields + amount, getMaxShields());
		for (final PlanetObserver obs : aObserverSet) {
			obs.shieldsChanged(this, aCurrentShields - previousShields);
		}
	}

	/**
	 * Changes the owner of this Planet to be the parameter Player. Passing null or the current owner will have no effect.
	 * 
	 * @param player
	 *            The Planet's new owner.
	 */
	public void changeOwner(final Player player)
	{
		if (player == null || player.equals(aOwner)) {
			return; // No change
		}
		// change the player
		aOwner = player;
		// Reset buildings
		deleteBuildings();
		// warn all observers
		for (final PlanetObserver observer : aObserverSet) {
			observer.planetCaptured(this, aOwner);
		}
	}

	/**
	 * Deletes the building currently occupying the given slot if that is a valid action.
	 * 
	 * @param slot
	 *            The slot to clear.
	 */
	public void deleteBuilding(final int slot)
	{
		if (!hasSlot(slot)) {
			return;
		}
		final Building toDelete = getBuildingAt(slot);
		if (toDelete != null) {
			toDelete.deregister();
		}
		aBuildings.put(slot, null);
	}

	/**
	 * Deletes all buildings currently on this Planet.
	 */
	public void deleteBuildings()
	{
		// This will initialize the map to all-null buildings
		for (int b = 0; b < aData.getNumOfBuildingSlots(); b++) {
			deleteBuilding(b);
		}
	}

	public void deregisterObserver(final PlanetObserver pObserver)
	{
		aObserverSet.remove(pObserver);
	}

	/**
	 * @return The building currently located at the given slot.
	 */
	public Building getBuildingAt(final int slot)
	{
		if (!hasSlot(slot)) {
			return null;
		}
		return aBuildings.get(slot);
	}

	/**
	 * @return The number of turns until the building at the given slot is completed.
	 */
	public Integer getBuildingProgress(final int slot)
	{
		if (!hasSlot(slot)) {
			return null;
		}
		return aBuildings.get(slot).getConstructionProgress();
	}

	/**
	 * @return The maps of slots -> buildings for all buildings currently on this Planet.
	 */
	public Map<Integer, Building> getBuildings()
	{
		return aBuildings;
	}

	/**
	 * @return This Planet's current health.
	 */
	public int getCurrentHealth()
	{
		return aCurrentHealth;
	}

	/**
	 * @return This Planets current shields.
	 */
	public int getCurrentShields()
	{
		return aCurrentShields;
	}

	/**
	 * @return The ratio of current shields on to max shields.
	 */
	public float getCurrentShieldsPercentage()
	{
		if (getMaxShields() <= 0) {
			return 0; // Prevent silent division by 0
		}
		return (float) aCurrentShields / (float) getMaxShields();
	}

	/**
	 * @return This Plaent's Data.
	 */
	public PlanetData getData()
	{
		return aData;
	}

	/**
	 * @return The rate at which this Planet regenerates shields, as defined by the current research level.
	 */
	public int getHealthRegenRate()
	{
		if (aOwner.isNullPlayer()) {
			return 0;
		}
		return aData.getHealthRegenRate(aOwner.getResearch());
	}

	/**
	 * @return This Planet's maximum health as defined by the current research level.
	 */
	public int getMaxHealth()
	{
		// TODO deal with research
		return aData.getBaseHealth();
	}

	/**
	 * @return The Planet's max shields, as defined by the current research level.
	 */
	public int getMaxShields()
	{
		int maxShields = 0;
		for (final Building b : aBuildings.values()) {
			if (b != null && b.isBuildingComplete()) {
				maxShields += b.getExtraShields();
			}
		}
		return maxShields;
	}

	/**
	 * @return The string representation of this Planet's type.
	 */
	public String getPlanetType()
	{
		return aData.getTitle();
	}

	/**
	 * @return The current rate at which this Planet is producing resources.
	 */
	public ResourceAmount getResourceRate()
	{
		ResourceAmount income = aData.getResourceRate().populateWith(getState());
		for (final Building b : aBuildings.values()) {
			if (b != null && b.isBuildingComplete()) {
				final ResourceAmount bIncome = b.getIncomeRate();
				if (bIncome != null) {
					income = income.add(bIncome);
				}
			}
		}
		return income;
	}

	/**
	 * @return The current rate at which this Planet is regenerating shields.
	 */
	public int getShieldRegenRate()
	{
		if (aOwner.isNullPlayer()) {
			return 0;
		}
		int shieldsRegen = 0;
		for (final Building b : aBuildings.values()) {
			if (b != null && b.isBuildingComplete()) {
				shieldsRegen += b.getShieldRegenerationRate();
			}
		}
		return shieldsRegen;
	}

	/**
	 * @return This Planet's shield sprite.
	 */
	public SpriteData getShieldSprite()
	{
		return getPlayer().getRaceData().getShieldSprite(getPlayer().getResearch(), getDimension());
	}

	/**
	 * @return The slot occupied by the parameter Building on this Planet; null if the Building is not on the Planet.
	 */
	public Integer getSlotForBuilding(final Building building)
	{
		if (building == null) {
			return null;
		}
		for (int slot = 0; slot < aData.getNumOfBuildingSlots(); slot++) {
			if (building.equals(getBuildingAt(slot))) {
				return slot;
			}
		}
		return null;
	}

	/**
	 * @return Whether the index falls within the Planet's Building range.
	 */
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
		if (!hasSlot(slot) || (isBuildingComplete(slot) && getBuildingAt(slot).getData().equals(building))) {
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
			addBuilding(slot, b);
		}
		b.incrementBuildingProgress();
		for (final PlanetObserver obs : aObserverSet) {
			obs.buildingsChanged(this);
		}
	}

	/**
	 * @return Whether the Planet is at max health.
	 */
	public boolean isAtMaxHealth()
	{
		return aCurrentHealth == getMaxHealth();
	}

	/**
	 * @return Whether the Planet is a max shields.
	 */
	public boolean isAtMaxShields()
	{
		return aCurrentShields == getMaxShields();
	}

	/**
	 * @return Whether the Building at the given slot has been completed.
	 */
	public boolean isBuildingComplete(final int slot)
	{
		return getBuildingAt(slot) != null && getBuildingAt(slot).isBuildingComplete();
	}

	/**
	 * @return Whether the given slot is currently free or occupied.
	 */
	public boolean isSlotFree(final int slot)
	{
		return hasSlot(slot) && aBuildings.get(slot) == null;
	}

	/**
	 * Populates this planet's buildings with the player race's initial buildings. Only called when generating a random game
	 * state, not when capturing.
	 */
	public void populateInitialBuildings()
	{
		final List<BuildingData> buildings = aOwner.getRaceData().getInitialBuildingData();
		int slot = 0;
		for (final BuildingData data : buildings) {
			if (!hasSlot(slot)) {
				break;
			}
			addBuilding(slot, new Building(getState(), this, data, true));
			slot++;
		}
	}

	public void registerObserver(final PlanetObserver pObserver)
	{
		aObserverSet.add(pObserver);
	}

	/**
	 * Removes the given amount from the Planet's health. The resulting amount is bounded between zero and max health as
	 * determined by the Planet type.
	 * 
	 * @param amount
	 *            the amount to remove.
	 */
	public void removeHealth(final int amount)
	{
		addHealth(-amount);
	}

	/**
	 * Removes the given amount from the Plaent's shields. The resulting amount is bounded between zero and max shields, as
	 * determined by the Ship type.
	 * 
	 * @param amount
	 *            The amount to remove from the shields.
	 */
	private void removeShields(final int amount)
	{
		addShields(-amount);
	}

	/**
	 * Damages the Planet as appropriate, removing first from the shields and then the overflow from the health.
	 * 
	 * @param damage
	 *            The damage to be dealt.
	 */
	public void takeDamange(final int damage)
	{
		removeHealth(Math.max(0, damage - aCurrentShields));
		removeShields(Math.min(aCurrentShields, damage));
	}

	@Override
	public Json toJson()
	{
		final Json j = super.toJson();
		j.setAttribute("planettype", aData.getType());
		j.setMapAttribute("buildings", aBuildings);
		j.setAttribute("health", aCurrentHealth);
		return j;
	}
}

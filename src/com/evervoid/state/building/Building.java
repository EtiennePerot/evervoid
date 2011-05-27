package com.evervoid.state.building;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVGameState;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;
import com.evervoid.state.prop.Planet;
import com.evervoid.utils.Pair;

/**
 * Buildings are built on a {@link Planet}, the have build cost and time. Once built they provide benefits, either by enabling
 * ship construction, research, or by improving the planet.
 */
public class Building implements Jsonable, Comparable<Building>
{
	/**
	 * How far along this building is on construction. 0 represents a building just queued and aData.buildTime is a fully
	 * constructed building.
	 */
	private int aBuildingProgress;
	/**
	 * This Building's Data.
	 */
	private final BuildingData aData;
	/**
	 * This Building's id the owner state.
	 */
	private final int aID;
	/**
	 * The Planet this building belongs to.
	 */
	private final Planet aPlanet;
	/**
	 * The progress on the ships currently being built in this Building.
	 */
	private Pair<ShipData, Integer> aShipProgress;
	/**
	 * The state this Building belongs to.
	 */
	private final EVGameState aState;

	/**
	 * Creates a building with the given parameters.
	 * 
	 * @param state
	 *            The state this building will belong to.
	 * @param planet
	 *            The planet this Building will be constructed on.
	 * @param data
	 *            The Building's Data.
	 * @param isBuilt
	 *            Whether the Building is already constructed.
	 */
	public Building(final EVGameState state, final Planet planet, final BuildingData data, final boolean isBuilt)
	{
		aState = state;
		aPlanet = planet;
		aData = data;
		aID = state.getNextBuildingID();
		aBuildingProgress = isBuilt ? aData.getBuildTime() : 0;
		state.registerBuilding(this);
	}

	/**
	 * Constructs a Building from the contents of the Json.
	 * 
	 * @param j
	 *            The Json from which to create the Building.
	 * @param state
	 *            The State to which this Building belongs.
	 */
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

	/**
	 * Removes this Building from its owning Planet.
	 */
	public void delete()
	{
		getPlanet().deleteBuilding(getPlanetSlot());
	}

	/**
	 * Deregisters this Building from the state.
	 */
	public void deregister()
	{
		aState.deregisterBuilding(getID());
	}

	/**
	 * @return The String representation of this Building's type.
	 */
	public String getBuildingType()
	{
		return aData.getType();
	}

	/**
	 * @return The construction progress on this Building.
	 */
	public int getConstructionProgress()
	{
		return aBuildingProgress;
	}

	/**
	 * @return This Building's data.
	 */
	public BuildingData getData()
	{
		return aData;
	}

	/**
	 * @return The amount of extra shields this Building adds to its Planet if any. If the building is not constructed, returns
	 *         0.
	 */
	public int getExtraShields()
	{
		return isBuildingComplete() ? aData.getExtraShields() : 0;
	}

	/**
	 * @return This Building's id.
	 */
	public int getID()
	{
		return aID;
	}

	/**
	 * @return The rate at which this Building produces income.
	 */
	public ResourceAmount getIncomeRate()
	{
		return aData.getIncome();
	}

	/**
	 * @return The Planet this Building exists on.
	 */
	public Planet getPlanet()
	{
		return aPlanet;
	}

	/**
	 * @return The Building slot this Building occupies on the Planet.
	 */
	public int getPlanetSlot()
	{
		return aPlanet.getSlotForBuilding(this);
	}

	/**
	 * @return The Player this Building belongs to.
	 */
	public Player getPlayer()
	{
		return aPlanet.getPlayer();
	}

	/**
	 * @return The percentage of construction currently completed.
	 */
	public float getProgressPercentage()
	{
		return (float) aBuildingProgress / (float) aData.getBuildTime();
	}

	/**
	 * @return A string representation of the percent of construction currently done.
	 */
	public String getProgressPercentageAsString()
	{
		return String.valueOf((int) (100f * getProgressPercentage())) + "%";
	}

	/**
	 * @return The rate at which this Building improves Planet Shield regeneration rates.
	 */
	public int getShieldRegenerationRate()
	{
		if (!isBuildingComplete()) {
			return 0;
		}
		return aData.getShieldRegen();
	}

	/**
	 * @return A String representation of the Building's construction percentage.
	 */
	public String getShipConstructionPercentageAsString()
	{
		return (int) (100f * getShipConstructionPrecentage()) + "%";
	}

	/**
	 * @return The Building's construction percentage.
	 */
	public float getShipConstructionPrecentage()
	{
		if (!isConstructingShip()) {
			return 0;
		}
		return (float) getShipProgress().getValue() / (float) getShipCurrentlyBuilding().getBaseBuildTime();
	}

	/**
	 * @return The ShipData of the Ship currently being built. The result is null if no Ship is being built.
	 */
	public ShipData getShipCurrentlyBuilding()
	{
		if (aShipProgress == null) {
			return null;
		}
		return aShipProgress.getKey();
	}

	/**
	 * @return The progress made on the ship currently being built.
	 */
	public Pair<ShipData, Integer> getShipProgress()
	{
		return aShipProgress;
	}

	/**
	 * @return The State this Building belongs to.
	 */
	public EVGameState getState()
	{
		return getPlanet().getState();
	}

	/**
	 * @return Increments the building progress if and only if the building is not already complete.
	 */
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

	/**
	 * @return Whether the Building is finished constructing.
	 */
	public boolean isBuildingComplete()
	{
		return aBuildingProgress >= aData.getBuildTime();
	}

	/**
	 * @return Whether the Building is currently working on a Ship.
	 */
	public boolean isConstructingShip()
	{
		return aShipProgress != null;
	}

	/**
	 * Resets all progress on the Ship currently being built.
	 */
	public void resetShipConstruction()
	{
		aShipProgress = null;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setAttribute("id", aID);
		j.setAttribute("player", getPlayer().getName());
		j.setAttribute("planet", aPlanet.getID());
		j.setAttribute("type", aData.getType());
		j.setAttribute("progress", aBuildingProgress);
		if (aShipProgress == null) {
			j.setAttribute("ship", null);
		}
		else {
			// Perfect example of autoformatter failing:
			j.setAttribute(
					"ship",
					new Json().setAttribute("name", aShipProgress.getKey().getType()).setAttribute("progress",
							aShipProgress.getValue()));
		}
		return j;
	}
}

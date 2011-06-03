package com.evervoid.state.data;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.building.Building;
import com.evervoid.state.player.ResourceAmount;

/**
 * BuildingData contains all the information necessary to creating a {@link Building}.
 */
public class BuildingData implements Jsonable
{
	/**
	 * @return The URL of the empty building icon.
	 */
	public static String getBlankBuildingIcon()
	{
		return "buildings/_empty.png";
	}

	/**
	 * The number of turns it takes to construct Buildings of this type.
	 */
	private final int aConstructionTime;
	/**
	 * The cost of constructing a Building of this type.
	 */
	private final ResourceAmount aCost;
	/**
	 * Shields provided by the building
	 */
	private final int aExtraShields;
	/**
	 * The income produced by Buildings of this type every turn.
	 */
	private final ResourceAmount aIncome;
	/**
	 * The race that can construct Buildings of this type.
	 */
	private final String aRace;
	/**
	 * Shield regeneration rate
	 */
	private final int aRegenShields;
	/**
	 * The List of Ships types that Buildings of this type may construct.
	 */
	private final List<String> aShipTypes;
	/**
	 * The in game title for this Building type.
	 */
	private final String aTitle;
	/**
	 * The Building type.
	 */
	private final String aType;

	/**
	 * Deserializaes a Building Data.
	 * 
	 * @param type
	 *            The Building type.
	 * @param race
	 *            The race that can construct this Building type.
	 * @param j
	 *            The Json containing information on the Building type.
	 */
	public BuildingData(final String type, final String race, final Json j)
	{
		aType = type;
		aRace = race;
		aTitle = j.getStringAttribute("title");
		aCost = new ResourceAmount(j.getAttribute("cost"));
		aConstructionTime = j.getIntAttribute("buildTime");
		if (j.hasAttribute("canbuild")) {
			aShipTypes = j.getStringListAttribute("canbuild");
		}
		else {
			aShipTypes = new ArrayList<String>();
		}
		aExtraShields = j.getIntAttribute("shields");
		if (j.hasAttribute("regen")) {
			aRegenShields = j.getIntAttribute("regen");
		}
		else {
			aRegenShields = 0;
		}
		if (j.hasAttribute("income") && !j.getAttribute("income").isNull()) {
			aIncome = new ResourceAmount(j.getAttribute("income"));
		}
		else {
			aIncome = null;
		}
	}

	/**
	 * @return The List of all Ship types this Building can construct.
	 */
	public List<String> getAvailableShipTypes()
	{
		return aShipTypes;
	}

	/**
	 * @return The number of turns it takes to construct Buildings of this type.
	 */
	public int getBuildTime()
	{
		return aConstructionTime;
	}

	/**
	 * @return The cost to construct Buildings of this type.
	 */
	public ResourceAmount getCost()
	{
		return aCost;
	}

	/**
	 * @return The extra shields produced by Buildings of this type.
	 */
	public int getExtraShields()
	{
		return aExtraShields;
	}

	/**
	 * @return The icon for this Building type.
	 */
	public SpriteData getIcon()
	{
		return new SpriteData("buildings/" + aRace + "/" + aType + ".png");
	}

	/**
	 * @return The income produced by Buildings of this type.
	 */
	public ResourceAmount getIncome()
	{
		return aIncome;
	}

	/**
	 * @return The shield regeneration rate for Buildings of this type.
	 */
	public int getShieldRegen()
	{
		return aRegenShields;
	}

	/**
	 * @return The in game name for this Building type.
	 */
	public String getTitle()
	{
		return aTitle;
	}

	/**
	 * @return The building type.
	 */
	public String getType()
	{
		return aType;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setAttribute("title", aTitle);
		j.setAttribute("cost", aCost);
		j.setAttribute("buildTime", aConstructionTime);
		j.setListAttribute("canbuild", aShipTypes);
		j.setAttribute("income", aIncome);
		j.setAttribute("regen", aRegenShields);
		j.setAttribute("shields", aExtraShields);
		return j;
	}
}

package com.evervoid.state.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.building.Building;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.Research;
import com.evervoid.state.player.ResourceAmount;

/**
 * RaceData contains the information pertinent to one of the races a {@link Player} may choose.
 */
public class RaceData implements Jsonable
{
	/**
	 * All the {@link Building} this race may construct, mapped to their type.
	 */
	private final Map<String, BuildingData> aBuildingData = new HashMap<String, BuildingData>();
	/**
	 * A list of Buildings the race may construct at the beginning of the game.
	 */
	private final List<BuildingData> aInitialBuildings = new ArrayList<BuildingData>();
	/**
	 * The resources this Race starts out with.
	 */
	private final ResourceAmount aInitialResources;
	/**
	 * All the research trees this race may research mapped to their names.
	 */
	private final Map<String, ResearchTreeData> aResearchTrees = new HashMap<String, ResearchTreeData>();
	/**
	 * All the ships this race may construct mapped to their types.
	 */
	private final Map<String, ShipData> aShipData = new HashMap<String, ShipData>();
	/**
	 * The Race's in game name.
	 */
	private final String aTitle;
	/**
	 * The type of trail ships of this race have.
	 */
	private final Map<String, TrailData> aTrailData = new HashMap<String, TrailData>();
	/**
	 * The race's type.
	 */
	private final String aType;
	/**
	 * The weapons available to this race mapped to their type.
	 */
	private final Map<String, WeaponData> aWeaponData = new HashMap<String, WeaponData>();

	/**
	 * Deserializes a RaceData from the Json and associates it with the given type.
	 * 
	 * @param race
	 *            The type of the race.
	 * @param j
	 *            The Json containing the pertinent information.
	 */
	RaceData(final String race, final Json j)
	{
		aType = race;
		aTitle = j.getStringAttribute("title");
		final Json shipJson = j.getAttribute("ships");
		for (final String ship : shipJson.getAttributes()) {
			aShipData.put(ship, new ShipData(ship, this, shipJson.getAttribute(ship)));
		}
		final Json trailJson = j.getAttribute("trails");
		for (final String trail : trailJson.getAttributes()) {
			aTrailData.put(trail, new TrailData(trail, aType, trailJson.getAttribute(trail)));
		}
		final Json weaponJson = j.getAttribute("weapons");
		for (final String weapon : weaponJson.getAttributes()) {
			aWeaponData.put(weapon, new WeaponData(weapon, aType, weaponJson.getAttribute(weapon)));
		}
		final Json researchJson = j.getAttribute("research");
		for (final String research : researchJson.getAttributes()) {
			aResearchTrees.put(research, new ResearchTreeData(research, aType, researchJson.getAttribute(research)));
		}
		final Json buildingJson = j.getAttribute("buildings");
		for (final String building : buildingJson.getAttributes()) {
			aBuildingData.put(building, new BuildingData(building, aType, buildingJson.getAttribute(building)));
		}
		aInitialResources = new ResourceAmount(j.getAttribute("initialResources"));
		for (final String buildType : j.getStringListAttribute("initialbuildings")) {
			final BuildingData data = getBuildingData(buildType);
			if (data != null) {
				aInitialBuildings.add(data);
			}
		}
	}

	/**
	 * @param building
	 *            the title of the building to get.
	 * @return The BuildingData associated with the give type, null if the element does not exist.
	 */
	public BuildingData getBuildingData(final String building)
	{
		return aBuildingData.get(building);
	}

	/**
	 * @return The names of all buildings this race can construct.
	 */
	public Set<String> getBuildings()
	{
		return aBuildingData.keySet();
	}

	/**
	 * @return The list of all Buildings this race can construct at the begining of the game.
	 */
	public List<BuildingData> getInitialBuildingData()
	{
		return aInitialBuildings;
	}

	/**
	 * @param style
	 *            the style of the icon to get.
	 * @return The URL of the race icon with the given style for this race.
	 */
	public String getRaceIcon(final String style)
	{
		return "icons/races/" + aType + "/" + style + ".png";
	}

	/**
	 * @param researchTree
	 *            The title of the research tree to get.
	 * @return The research tree associated with the given type, null if there is no such element.
	 */
	public ResearchTreeData getResearchTree(final String researchTree)
	{
		return aResearchTrees.get(researchTree);
	}

	/**
	 * @param research
	 *            The research level at which to get the sprite.
	 * @param dimension
	 *            The desired size of the sprite.
	 * @precondition dimension is a square dimension smaller than a 5X5.
	 * @return The shield sprite for a Ship of the given dimension at the given research level.
	 */
	public SpriteData getShieldSprite(final Research research, final Dimension dimension)
	{
		// TODO - Take research into account, make it race-specific
		if (dimension.sameAs(1, 1)) {
			return new SpriteData("shields/shield_1x1.png");
		}
		if (dimension.sameAs(2, 2)) {
			return new SpriteData("shields/shield_2x2.png");
		}
		if (dimension.sameAs(3, 3)) {
			return new SpriteData("shields/shield_3x3.png");
		}
		if (dimension.sameAs(4, 4)) {
			return new SpriteData("shields/shield_4x4.png");
		}
		return null;
	}

	/**
	 * @param shipType
	 *            The title of the ship data to get.
	 * @return The ShipData for the given type, null if there is no such element.
	 */
	public ShipData getShipData(final String shipType)
	{
		return aShipData.get(shipType);
	}

	/**
	 * @return The set of all Ship types this race can construct.
	 */
	public Set<String> getShipTypes()
	{
		return aShipData.keySet();
	}

	/**
	 * @return The resources this race starts the game with.
	 */
	public ResourceAmount getStartResources()
	{
		return aInitialResources;
	}

	/**
	 * @return This race's in game name.
	 */
	public String getTitle()
	{
		return aTitle;
	}

	/**
	 * @param trailType
	 *            The title of the trail type to get.
	 * @return The TrailData for the given trail type, null if there is no such element.
	 */
	public TrailData getTrailData(final String trailType)
	{
		return aTrailData.get(trailType);
	}

	/**
	 * @return The set of all trail types for this race.
	 */
	public Set<String> getTrailTypes()
	{
		return aTrailData.keySet();
	}

	/**
	 * @return The race's type.
	 */
	public String getType()
	{
		return aType;
	}

	/**
	 * @param weaponType
	 *            The title of the weapon type to get.
	 * @return The WeaponData associated with the given type, null if there is no such element.
	 */
	public WeaponData getWeaponData(final String weaponType)
	{
		return aWeaponData.get(weaponType);
	}

	/**
	 * @return THe set of all weapon types for this race.
	 */
	public Set<String> getWeaponTypes()
	{
		return aWeaponData.keySet();
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setMapAttribute("ships", aShipData);
		j.setMapAttribute("trails", aTrailData);
		j.setMapAttribute("research", aResearchTrees);
		j.setAttribute("title", aTitle);
		j.setAttribute("initialResources", aInitialResources);
		j.setMapAttribute("buildings", aBuildingData);
		j.setMapAttribute("weapons", aWeaponData);
		final List<String> initialBuildings = new ArrayList<String>(aInitialBuildings.size());
		for (final BuildingData data : aInitialBuildings) {
			initialBuildings.add(data.getType());
		}
		j.setListAttribute("initialbuildings", initialBuildings);
		return j;
	}
}

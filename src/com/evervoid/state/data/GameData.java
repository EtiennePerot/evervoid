package com.evervoid.state.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Color;
import com.evervoid.utils.LoggerUtils;
import com.evervoid.utils.MathUtils;

public class GameData implements Jsonable
{
	/**
	 * This method tests loading the default game data
	 */
	public static void main(final String[] args)
	{
		System.out.println("Loading GameData...");
		GameData data = null;
		try {
			data = new GameData();
		}
		catch (final BadJsonInitialization e1) {
			e1.printStackTrace();
		}
		System.out.println("GameData loaded. Re-serializing:");
		final Json jData = data.toJson();
		System.out.println(jData.toPrettyString());
		System.out.println("Recreating GameData based on re-serialized data...");
		GameData data2 = null;
		try {
			data2 = new GameData(jData);
		}
		catch (final BadJsonInitialization e) {
			e.printStackTrace();
		}
		final Json jData2 = data2.toJson();
		System.out.println(jData2.toPrettyString());
		System.out.println("Comparing both GameData hashes: ");
		System.out.println("Initial GameData: " + jData.getHash());
		System.out.println("Re-read GameData: " + jData2.getHash());
		System.out.println("Match: " + jData2.equals(jData));
	}

	private final int aJumpCost;
	private final Map<String, PlanetData> aPlanetData = new HashMap<String, PlanetData>();
	private final Map<String, Color> aPlayerColors = new HashMap<String, Color>();
	private final Map<String, RaceData> aRaceData = new HashMap<String, RaceData>();
	private final Map<String, ResourceData> aResources = new HashMap<String, ResourceData>();
	private final Map<String, StarData> aStarData = new HashMap<String, StarData>();
	private final int aTurnLength;

	/**
	 * Loads default game data from schema/gamedata.json
	 * 
	 * @throws BadJsonInitialization
	 */
	public GameData() throws BadJsonInitialization
	{
		this("schema/gamedata.json");
	}

	/**
	 * Creates a new game data object from Json
	 * 
	 * @param j
	 *            Parsed Json containing the game data
	 * @throws BadJsonInitialization
	 */
	public GameData(final Json j) throws BadJsonInitialization
	{
		try {
			final Json starJson = j.getAttribute("star");
			for (final String star : starJson.getAttributes()) {
				aStarData.put(star, new StarData(star, starJson.getAttribute(star)));
			}
			final Json planetJson = j.getAttribute("planet");
			for (final String planet : planetJson.getAttributes()) {
				aPlanetData.put(planet, new PlanetData(planet, planetJson.getAttribute(planet)));
			}
			final Json raceJson = j.getAttribute("race");
			for (final String race : raceJson.getAttributes()) {
				aRaceData.put(race, new RaceData(race, raceJson.getAttribute(race)));
			}
			final Json resourceJson = j.getAttribute("resources");
			for (final String resource : resourceJson.getAttributes()) {
				aResources.put(resource, new ResourceData(resource, resourceJson.getAttribute(resource)));
			}
			final Json colorJson = j.getAttribute("playercolors");
			for (final String color : colorJson.getAttributes()) {
				aPlayerColors.put(color, new Color(color, colorJson.getAttribute(color)));
			}
		}
		catch (final Exception e) {
			LoggerUtils.getLogger().log(Level.SEVERE, "Caught error in Game Data loading, syntax is incorrect", e);
			throw new BadJsonInitialization();
		}
		aTurnLength = j.getIntAttribute("turnLength");
		aJumpCost = j.getIntAttribute("jumpCost");
	}

	public GameData(final String filename) throws BadJsonInitialization
	{
		this(Json.fromFile(filename));
	}

	public BuildingData getBuildingData(final String race, final String building)
	{
		return aRaceData.get(race).getBuildingData(building);
	}

	public int getJumpCost()
	{
		return aJumpCost;
	}

	public PlanetData getPlanetData(final String planetType)
	{
		return aPlanetData.get(planetType);
	}

	public Set<String> getPlanetTypes()
	{
		return aPlanetData.keySet();
	}

	public Color getPlayerColor(final String colorname)
	{
		return aPlayerColors.get(colorname);
	}

	public Set<String> getPlayerColors()
	{
		return aPlayerColors.keySet();
	}

	public RaceData getRaceData(final String raceType)
	{
		return aRaceData.get(raceType);
	}

	public Set<String> getRaceTypes()
	{
		return aRaceData.keySet();
	}

	public String getRandomColor()
	{
		return MathUtils.getRandomElement(aPlayerColors.keySet());
	}

	public String getRandomRace()
	{
		return MathUtils.getRandomElement(aRaceData.keySet());
	}

	public ResourceData getResourceData(final String resourceType)
	{
		return aResources.get(resourceType);
	}

	public Set<String> getResources()
	{
		return aResources.keySet();
	}

	public StarData getStarData(final String starType)
	{
		return aStarData.get(starType);
	}

	public Set<String> getStarTypes()
	{
		return aStarData.keySet();
	}

	public int getTurnLength()
	{
		return aTurnLength;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setMapAttribute("star", aStarData);
		j.setMapAttribute("planet", aPlanetData);
		j.setMapAttribute("race", aRaceData);
		j.setMapAttribute("playercolors", aPlayerColors);
		j.setMapAttribute("resources", aResources);
		j.setAttribute("turnLength", aTurnLength);
		j.setAttribute("jumpCost", aJumpCost);
		return j;
	}
}

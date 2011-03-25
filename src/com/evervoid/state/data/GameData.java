package com.evervoid.state.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Color;
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

	private final Map<String, PlanetData> aPlanetData = new HashMap<String, PlanetData>();
	private final Map<String, Color> aPlayerColors = new HashMap<String, Color>();
	private final Map<String, RaceData> aRaceData = new HashMap<String, RaceData>();
	private final Set<String> aResources = new HashSet<String>();
	private final Map<String, StarData> aStarData = new HashMap<String, StarData>();

	/**
	 * Loads default game data from schema/gamedata.json
	 * 
	 * @throws BadJsonInitialization
	 */
	public GameData() throws BadJsonInitialization
	{
		this("res/schema/gamedata.json");
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
			final List<String> resourceJson = j.getStringListAttribute("resources");
			for (final String resource : resourceJson) {
				aResources.add(resource);
			}
			final Json colorJson = j.getAttribute("playercolors");
			for (final String color : colorJson.getAttributes()) {
				aPlayerColors.put(color, new Color(colorJson.getAttribute(color)));
			}
		}
		catch (final Exception e) {
			Logger.getLogger("").log(Level.SEVERE, "Caught error in Game Data loading, syntax is incorrect", e);
			throw new BadJsonInitialization();
		}
	}

	public GameData(final String filename) throws BadJsonInitialization
	{
		this(Json.fromFile(filename));
	}

	public BuildingData getBuildingData(final String race, final String building)
	{
		return aRaceData.get(race).getBuildingData(building);
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
		return (String) MathUtils.getRandomElement(aPlayerColors.keySet());
	}

	public String getRandomRace()
	{
		return (String) MathUtils.getRandomElement(aRaceData.keySet());
	}

	public Set<String> getResources()
	{
		return aResources;
	}

	public StarData getStarData(final String starType)
	{
		return aStarData.get(starType);
	}

	public Set<String> getStarTypes()
	{
		return aStarData.keySet();
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setMapAttribute("star", aStarData);
		j.setMapAttribute("planet", aPlanetData);
		j.setMapAttribute("race", aRaceData);
		j.setMapAttribute("playercolors", aPlayerColors);
		j.setStringListAttribute("resources", aResources);
		return j;
	}
}

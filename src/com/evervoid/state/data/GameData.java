package com.evervoid.state.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class GameData implements Jsonable
{
	/**
	 * This method tests loading the default game data
	 */
	public static void main(final String[] args)
	{
		System.out.println("Loading GameData...");
		final GameData data = new GameData();
		System.out.println("GameData loaded. Re-serializing:");
		final Json jData = data.toJson();
		System.out.println(jData.toPrettyString());
		System.out.println("Recreating GameData based on re-serialized data...");
		final GameData data2 = new GameData(jData);
		final Json jData2 = data2.toJson();
		System.out.println(jData2.toPrettyString());
		System.out.println("Comparing both GameData hashes: ");
		System.out.println("Initial GameData: " + jData.getHash());
		System.out.println("Re-read GameData: " + jData2.getHash());
		System.out.println("Match: " + jData2.equals(jData));
	}

	private final Map<String, PlanetData> aPlanetData = new HashMap<String, PlanetData>();
	private final Map<String, RaceData> aRaceData = new HashMap<String, RaceData>();
	private final Map<String, StarData> aStarData = new HashMap<String, StarData>();

	/**
	 * Loads default game data from schema/gamedata.json
	 */
	public GameData()
	{
		this(Json.fromFile("res/schema/gamedata.json"));
	}

	/**
	 * Creates a new game data object from Json
	 * 
	 * @param j
	 *            Parsed Json containing the game data
	 */
	public GameData(final Json j)
	{
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
	}

	public PlanetData getPlanetData(final String planetType)
	{
		return aPlanetData.get(planetType);
	}

	public Set<String> getPlanetTypes()
	{
		return aPlanetData.keySet();
	}

	public RaceData getRaceData(final String raceType)
	{
		return aRaceData.get(raceType);
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
		return new Json().setMapAttribute("star", aStarData).setMapAttribute("planet", aPlanetData)
				.setMapAttribute("race", aRaceData);
	}
}

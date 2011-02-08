package com.evervoid.gamedata;

import java.util.HashMap;
import java.util.Map;

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
		System.out.println("GameData loaded. Pretty String:");
		System.out.println(data.toJson().toPrettyString());
	}

	private final Map<String, PlanetData> aPlanetData = new HashMap<String, PlanetData>();
	private final Map<String, ShipData> aShipData = new HashMap<String, ShipData>();

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
		final Json planetJson = j.getAttribute("planet");
		for (final String planet : planetJson.getAttributes()) {
			aPlanetData.put(planet, new PlanetData(planet, planetJson.getAttribute(planet)));
		}
		final Json shipJson = j.getAttribute("ship");
		for (final String ship : shipJson.getAttributes()) {
			aShipData.put(ship, new ShipData(ship, shipJson.getAttribute(ship)));
		}
	}

	public PlanetData getPlanetData(final String planetType)
	{
		return aPlanetData.get(planetType);
	}

	public ShipData getShipData(final String shipType)
	{
		return aShipData.get(shipType);
	}

	@Override
	public Json toJson()
	{
		// TODO: Complete
		return new Json().setMapAttribute("planet", aPlanetData).setMapAttribute("ship", aShipData);
	}
}

package com.evervoid.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.gamedata.GameData;
import com.evervoid.gamedata.PlanetData;
import com.evervoid.gamedata.RaceData;
import com.evervoid.gamedata.StarData;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Prop;

public class EverVoidGameState implements Jsonable
{
	public static void main(final String[] args)
	{
		System.out.println("Creating test game state...");
		final EverVoidGameState testState = new EverVoidGameState();
		System.out.println("Creating test game state created, printing.");
		System.out.println(testState.toJson().toPrettyString());
	}

	private final Map<Integer, Prop> aAllProps = new HashMap<Integer, Prop>();
	private final Galaxy aGalaxy;
	private final GameData aGameData;
	private final Player aNullPlayer;
	private final List<Player> aPlayerList;
	private final String neutralPlayerName = "NullPlayer";

	/**
	 * Default constructor, creates a fully randomized galaxy with solar systems and planets in it.
	 */
	public EverVoidGameState()
	{
		aGameData = new GameData(); // Game data must always be loaded first
		aPlayerList = new ArrayList<Player>();
		aNullPlayer = new Player(neutralPlayerName, this);
		aPlayerList.add(aNullPlayer);
		aPlayerList.add(new Player("Player1", this));
		aPlayerList.add(new Player("Player2", this));
		aGalaxy = new Galaxy(this);
		aGalaxy.populateRandomly();
	}

	/**
	 * Restore a game state from a serialized state
	 * 
	 * @param json
	 *            The Json representation of the game state
	 */
	public EverVoidGameState(final Json json)
	{
		aGameData = new GameData(json.getAttribute("gamedata"));
		aGalaxy = Galaxy.fromJson(json.getAttribute("galaxy"), this);
		final Json players = json.getAttribute("players");
		aPlayerList = new ArrayList<Player>(players.size());
		for (final Json p : players) {
			aPlayerList.add(Player.fromJson(p, this));
		}
		if (getPlayerByName(neutralPlayerName) != null) {
			aNullPlayer = getPlayerByName(neutralPlayerName);
		}
		else {
			aNullPlayer = new Player(neutralPlayerName, this);
			aPlayerList.add(aNullPlayer);
		}
	}

	/**
	 * Overloaded constructor using specified playerList and galaxy.
	 * 
	 * @param playerList
	 *            A list containing all the players.
	 * @param galaxy
	 *            A galaxy to generate the game state upon.
	 */
	public EverVoidGameState(final List<Player> playerList, final Galaxy galaxy)
	{
		aGameData = new GameData(); // Game data must always be loaded first
		aNullPlayer = new Player(neutralPlayerName, this);
		aPlayerList = new ArrayList<Player>(playerList);
		aPlayerList.add(aNullPlayer);
		aGalaxy = galaxy;
	}

	@Override
	public EverVoidGameState clone()
	{
		// TODO actually clone. Might wanna just serialize to Json and then back out; that's actually the same thing as the hack
		// that Robillard taught us...
		return this;
	}

	public Galaxy getGalaxy()
	{
		return aGalaxy;
	}

	/**
	 * @return The neutral (null) player
	 */
	public Player getNullPlayer()
	{
		return aNullPlayer;
	}

	/**
	 * @param planetType
	 *            The type of planet
	 * @return The PlanetData object corresponding to that planet type
	 */
	public PlanetData getPlanetData(final String planetType)
	{
		return aGameData.getPlanetData(planetType);
	}

	/**
	 * @return Available planet types
	 */
	public Set<String> getPlanetTypes()
	{
		return aGameData.getPlanetTypes();
	}

	/**
	 * Returns a Player by his/her name
	 * 
	 * @param name
	 *            The name to search for
	 * @return The player object
	 */
	public Player getPlayerByName(final String name)
	{
		for (final Player p : aPlayerList) {
			if (p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}

	public int getPropID()
	{
		// TODO - comment plz. What is this even supposed to do? -vbonnet
		if (aAllProps.isEmpty()) {
			return 0;
		}
		int maxId = Integer.MIN_VALUE;
		for (final Integer id : aAllProps.keySet()) {
			maxId = Math.max(id, id);
		}
		return maxId + 1;
	}

	/**
	 * @param raceType
	 *            The type of race
	 * @return The RaceData object corresponding to that race type
	 */
	public RaceData getRaceData(final String raceType)
	{
		return aGameData.getRaceData(raceType);
	}

	/**
	 * @return A randomly-selected player
	 */
	Player getRandomPlayer()
	{
		return (Player) MathUtils.getRandomElement(aPlayerList);
	}

	/**
	 * @param point
	 *            A 3D point in space.
	 * @return The solar system contained at the specified point.
	 */
	public SolarSystem getSolarSystem(final Point3D point)
	{
		return aGalaxy.getSolarSystem(point);
	}

	/**
	 * @param starType
	 *            The type of star
	 * @return The StarData object corresponding to that star type
	 */
	public StarData getStarData(final String starType)
	{
		return aGameData.getStarData(starType);
	}

	/**
	 * @return Available star types
	 */
	public Set<String> getStarTypes()
	{
		return aGameData.getStarTypes();
	}

	/**
	 * @return A temporary solar system. (Used for development).
	 */
	public SolarSystem getTempSolarSystem()
	{
		return aGalaxy.getTempSolarSystem();
	}

	/**
	 * Adds a prop to the game state's list of props
	 * 
	 * @param prop
	 *            The prop to add
	 * @return Whether registration was successful or not
	 */
	public boolean registerProp(final Prop prop)
	{
		// put always adds an element, hence the return is always true. Put also returns the previous element associated with
		// the key; if you truly want some kind of boolean logic, then compare prop to what put returned.
		aAllProps.put(prop.getID(), prop);
		return true;
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("gamedata", aGameData).setAttribute("galaxy", aGalaxy)
				.setListAttribute("players", aPlayerList);
	}
}

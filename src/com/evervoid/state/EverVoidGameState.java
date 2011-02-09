package com.evervoid.state;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.gamedata.GameData;
import com.evervoid.gamedata.PlanetData;
import com.evervoid.gamedata.RaceData;
import com.evervoid.state.player.Player;

public class EverVoidGameState
{
	private final Galaxy aGalaxy;
	private final GameData aGameData;
	private final Player aNullPlayer;
	private final List<Player> aPlayerList;

	/**
	 * Default constructor, creates a fully randomized galaxy with solar systems and planets in it.
	 */
	public EverVoidGameState()
	{
		aGameData = new GameData(); // Game data must always be loaded first
		aPlayerList = new ArrayList<Player>();
		aNullPlayer = new Player("NullPlayer", this);
		aPlayerList.add(aNullPlayer);
		aPlayerList.add(new Player("Player1", this));
		aPlayerList.add(new Player("Player2", this));
		aGalaxy = Galaxy.createRandomGalaxy(this);
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
		aNullPlayer = new Player("NullPlayer", this);
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
		// TODO make return correct solar system
		return aGalaxy.getSolarSystem(point);
	}

	/**
	 * @return A temporary solar system. (Used for development).
	 */
	public SolarSystem getTempSolarSystem()
	{
		return aGalaxy.getTempSolarSystem();
	}
}

package com.evervoid.state;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.gamedata.GameData;
import com.evervoid.gamedata.PlanetData;
import com.evervoid.gamedata.ShipData;
import com.evervoid.state.player.Player;

public class EverVoidGameState
{
	private final Galaxy aGalaxy;
	private final GameData aGameData;
	private final List<Player> aPlayerList;

	/**
	 * Default constructor, creates a fully randomized galaxy with solar systems and planets in it.
	 */
	public EverVoidGameState()
	{
		aGameData = new GameData(); // Game data must always be loaded first
		aPlayerList = new ArrayList<Player>();
		aPlayerList.add(new Player("Player1"));
		aPlayerList.add(new Player("Player2"));
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
		aGalaxy = galaxy;
		// create a new ArrayList and copy all of playerList into it
		aPlayerList = new ArrayList<Player>(playerList);
	}

	@Override
	public EverVoidGameState clone()
	{
		// TODO actually clone
		return this;
	}

	public Galaxy getGalaxy()
	{
		return aGalaxy;
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

	Player getRandomPlayer()
	{
		return (Player) MathUtils.getRandomElement(aPlayerList);
	}

	/**
	 * @param planetType
	 *            The type of ship
	 * @return The ShipData object corresponding to that ship type
	 */
	public ShipData getShipData(final String shipType)
	{
		return aGameData.getShipData(shipType);
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

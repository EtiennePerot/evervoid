package com.evervoid.state;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.state.player.Player;

public class EverVoidGameState
{
	private final Galaxy aGalaxy;
	private final List<Player> aPlayerList;
	private final SolarSystem aTempSolarSystem = new SolarSystem(64, 32);

	/**
	 * Default constructor, simply creates a brand new random galaxy with solar systems and planets in.
	 */
	public EverVoidGameState()
	{
		aPlayerList = new ArrayList<Player>();
		aPlayerList.add(new Player("EverVoidGame"));
		aGalaxy = Galaxy.createRandomGalaxy();
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
		aGalaxy = galaxy;
		// create a new ArrayList and copy all of playerList into it
		aPlayerList = new ArrayList<Player>();
		aPlayerList.addAll(playerList);
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
		return aTempSolarSystem;
	}
}

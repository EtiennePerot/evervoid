package com.evervoid.state;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.state.player.Player;

public class EverVoidGameState
{
	private final SolarSystem aTempSolarSystem = new SolarSystem(64, 32);
	private final Galaxy fGalaxy;
	private final List<Player> fPlayerList;

	/**
	 * Default constructor, simply creates a brand new random galaxy with solar systems and planets in.
	 */
	public EverVoidGameState()
	{
		fPlayerList = new ArrayList<Player>();
		fPlayerList.add(new Player("EverVoidGame"));
		fGalaxy = Galaxy.createRandomGalaxy();
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
		fGalaxy = galaxy;
		// create a new ArrayList and copy all of playerList into it
		fPlayerList = new ArrayList<Player>();
		fPlayerList.addAll(playerList);
	}

	@Override
	public EverVoidGameState clone()
	{
		// TODO actually clone
		return this;
	}

	public Galaxy getGalaxy()
	{
		return fGalaxy;
	}

	/**
	 * @param point
	 *            A 3D point in space.
	 * @return The solar system contained at the specified point.
	 */
	public SolarSystem getSolarSystem(final Point3D point)
	{
		// TODO make return correct solar system
		return fGalaxy.getSolarSystem(point);
	}

	/**
	 * @return A temporary solar system. (Used for development).
	 */
	public SolarSystem getTempSolarSystem()
	{
		return aTempSolarSystem;
	}
}

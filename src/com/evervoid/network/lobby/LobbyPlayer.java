package com.evervoid.network.lobby;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Color;
import com.jme3.network.connection.Client;

public class LobbyPlayer implements Jsonable
{
	private final Client aClient;
	private final Color aColor;
	private final boolean aIsAdmin;
	private boolean aIsReady;
	private final String aNickname;
	private String aRace;

	/**
	 * Server-side LobbyPlayer constructor; does include Client reference
	 * 
	 * @param client
	 *            Reference to jME Client
	 * @param nickname
	 *            Nickname of the player (may change)
	 * @param race
	 *            Race of the player
	 * @param color
	 *            Color of the player
	 */
	LobbyPlayer(final Client client, final String nickname, final String race, final Color color)
	{
		aClient = client;
		aNickname = nickname;
		aRace = race;
		aColor = color;
		aIsAdmin = true;
		aIsReady = false;
	}

	/**
	 * Deserialize a LobbyPlayer object out of a Json representation. This is done on the client side only, so aClient is null
	 * here.
	 * 
	 * @param j
	 *            The Json representation of the LobbyPlayer
	 */
	public LobbyPlayer(final Json j)
	{
		aClient = null;
		aColor = new Color(j.getAttribute("color"));
		aIsAdmin = j.getBooleanAttribute("admin");
		aNickname = j.getStringAttribute("nickname");
		aRace = j.getStringAttribute("race");
		aIsReady = j.getBooleanAttribute("ready");
	}

	public Client getClient()
	{
		return aClient;
	}

	public Color getColor()
	{
		return aColor;
	}

	public String getNickname()
	{
		return aNickname;
	}

	public String getRace()
	{
		return aRace;
	}

	public boolean isAdmin()
	{
		return aIsAdmin;
	}

	public boolean isReady()
	{
		return aIsReady;
	}

	/**
	 * Changes this player's race
	 * 
	 * @param race
	 *            The player's race
	 * @return Whether this update changed anything
	 */
	public boolean setRace(final String race)
	{
		final boolean changed = !aRace.equals(race);
		aRace = race;
		return changed;
	}

	/**
	 * Changes this player's ready status
	 * 
	 * @param ready
	 *            Ready or not
	 * @return Whether this update changed anything
	 */
	public boolean setReady(final boolean ready)
	{
		final boolean changed = aIsReady != ready;
		aIsReady = ready;
		return changed;
	}

	@Override
	public Json toJson()
	{
		// Careful: Do not serialize aClient
		return new Json().setStringAttribute("nickname", aNickname).setStringAttribute("race", aRace)
				.setAttribute("color", aColor).setBooleanAttribute("admin", aIsAdmin).setBooleanAttribute("ready", aIsReady);
	}
}

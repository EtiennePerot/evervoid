package com.evervoid.network.lobby;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Color;
import com.jme3.network.HostedConnection;

public class LobbyPlayer implements Jsonable
{
	private final HostedConnection aClient;
	private String aColor;
	private final boolean aIsAdmin;
	private boolean aIsReady;
	private final LobbyState aLobbyState;
	private String aNickname;
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
	LobbyPlayer(final LobbyState state, final HostedConnection client, final String nickname, final String race,
			final String color)
	{
		aLobbyState = state;
		aClient = client;
		aNickname = nickname;
		aRace = race;
		aColor = color;
		aIsAdmin = true;
		aIsReady = false;
	}

	/**
	 * Deserialize a LobbyPlayer object out of a Json representation. This is done on the client side, so aClient is null here.
	 * 
	 * @param j
	 *            The Json representation of the LobbyPlayer
	 */
	public LobbyPlayer(final LobbyState state, final Json j)
	{
		aLobbyState = state;
		aClient = null;
		aColor = j.getStringAttribute("color");
		aIsAdmin = j.getBooleanAttribute("admin");
		aNickname = j.getStringAttribute("nickname");
		aRace = j.getStringAttribute("race");
		aIsReady = j.getBooleanAttribute("ready");
	}

	public HostedConnection getClient()
	{
		return aClient;
	}

	public Color getColor()
	{
		return aLobbyState.getGameData().getPlayerColor(aColor);
	}

	public String getColorName()
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
	 * Changes this player's color
	 * 
	 * @param color
	 *            The enw player color
	 * @return Whether this update changed anything
	 */
	public boolean setColor(final String color)
	{
		final boolean changed = !aColor.equals(color);
		aColor = color;
		return changed;
	}

	/**
	 * Changes this player's nickname
	 * 
	 * @param nickname
	 *            The player's nickname
	 * @return Whether this update changed anything
	 */
	public boolean setNickname(final String nickname)
	{
		final boolean changed = !aNickname.equals(nickname);
		aNickname = nickname;
		return changed;
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
		return new Json().setAttribute("nickname", aNickname).setAttribute("race", aRace).setAttribute("color", aColor)
				.setAttribute("admin", aIsAdmin).setAttribute("ready", aIsReady);
	}
}

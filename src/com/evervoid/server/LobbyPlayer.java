package com.evervoid.server;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Color;
import com.jme3.network.connection.Client;

public class LobbyPlayer implements Jsonable
{
	private final Client aClient;
	private final Color aColor;
	private final boolean aIsAdmin;
	private final String aNickname;
	private final String aRace;

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
	}

	Client getClient()
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

	@Override
	public Json toJson()
	{
		// Careful: Do not serialize aClient
		return new Json().setStringAttribute("nickname", aNickname).setStringAttribute("race", aRace)
				.setAttribute("color", aColor).setBooleanAttribute("admin", aIsAdmin);
	}
}

package com.evervoid.network.lobby;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.data.GameData;
import com.jme3.network.connection.Client;

public class LobbyState implements Jsonable, Iterable<LobbyPlayer>
{
	private final GameData aGameData;
	private final List<LobbyPlayer> aLobbyPlayers = new ArrayList<LobbyPlayer>();
	private final String aServerName;

	public LobbyState(final GameData gamedata, final String servername)
	{
		aGameData = gamedata;
		aServerName = servername;
	}

	public LobbyState(final Json j)
	{
		this(new GameData(j.getAttribute("gamedata")), j.getStringAttribute("servername"));
		for (final Json p : j.getListAttribute("players")) {
			aLobbyPlayers.add(new LobbyPlayer(this, p));
		}
	}

	public LobbyPlayer addPlayer(final Client client, final String nickname)
	{
		final LobbyPlayer newPlayer = new LobbyPlayer(this, client, nickname, aGameData.getRandomRace(), getAvailableColor());
		aLobbyPlayers.add(newPlayer);
		return newPlayer;
	}

	/**
	 * @return A random non-taken color name
	 */
	private String getAvailableColor()
	{
		String color = null;
		while (color == null || isColorTaken(color, null)) {
			color = aGameData.getRandomColor();
		}
		return color;
	}

	public GameData getGameData()
	{
		return aGameData;
	}

	public int getNumOfPlayers()
	{
		return aLobbyPlayers.size();
	}

	public LobbyPlayer getPlayerByClient(final Client client)
	{
		for (final LobbyPlayer player : aLobbyPlayers) {
			if (player.getClient().equals(client)) {
				return player;
			}
		}
		return null;
	}

	public List<LobbyPlayer> getPlayers()
	{
		return aLobbyPlayers;
	}

	public String getServerName()
	{
		return aServerName;
	}

	/**
	 * Returns whether a color name is already used by a player or not
	 * 
	 * @param colorname
	 *            The color name
	 * @param ignore
	 *            Optional player to exclude from the lookup (null to disable)
	 * @return Whether this color is taken or not
	 */
	private boolean isColorTaken(final String colorname, final LobbyPlayer ignore)
	{
		for (final LobbyPlayer player : aLobbyPlayers) {
			if (!player.equals(ignore) && player.getColorName().equals(colorname)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<LobbyPlayer> iterator()
	{
		return aLobbyPlayers.iterator();
	}

	public void removePlayer(final Client client)
	{
		if (getPlayerByClient(client) != null) {
			removePlayer(getPlayerByClient(client));
		}
	}

	public void removePlayer(final LobbyPlayer player)
	{
		aLobbyPlayers.remove(player);
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("gamedata", aGameData).setListAttribute("players", aLobbyPlayers)
				.setStringAttribute("servername", aServerName);
	}

	/**
	 * Updates a LobbyPlayer's info from a received Json message
	 * 
	 * @param client
	 *            The client to update
	 * @param update
	 *            The message contents
	 * @return True if the player's info was valid and different from what it already was (thus, it is time to send the new info
	 *         to clients)
	 */
	public boolean updatePlayer(final Client client, final Json update)
	{
		final LobbyPlayer player = getPlayerByClient(client);
		if (player == null) {
			return false; // Invalid player
		}
		final String race = update.getStringAttribute("race");
		if (aGameData.getRaceData(race) == null) {
			return false; // Invalid race
		}
		final String color = update.getStringAttribute("color");
		if (aGameData.getPlayerColor(color) == null || isColorTaken(color, player)) {
			return false; // Invalid color
		}
		boolean changed = false;
		// It is important to do this in the "setter() || changed" order to make sure the setter gets called.
		// Can't put them on one line either or else some setters might not get called.
		changed = player.setRace(race) || changed;
		changed = player.setReady(update.getBooleanAttribute("ready")) || changed;
		changed = player.setColor(color) || changed;
		return changed;
	}
}
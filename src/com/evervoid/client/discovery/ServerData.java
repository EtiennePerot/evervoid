package com.evervoid.client.discovery;

/**
 * Simple data structure used to hold server information when discovered through the discovery service.
 */
public class ServerData
{
	/**
	 * The server's address
	 */
	public final String hostName;
	/**
	 * Whether the server is currently in-game (as opposed to in-lobby)
	 */
	public final boolean inGame;
	/**
	 * Ping time (in nanoseconds) to the server
	 */
	public final long ping;
	/**
	 * Number of players currently on the server
	 */
	public final int players;
	/**
	 * User-friendly name of the server
	 */
	public final String serverName;

	/**
	 * ServerData constructor; simply initializes the data structure
	 * 
	 * @param hostName
	 *            The server's address
	 * @param serverName
	 *            User-friendly name of the server
	 * @param players
	 *            Number of players currently on the server
	 * @param inGame
	 *            Whether the server is currently in-game (as opposed to in-lobby)
	 * @param ping
	 *            Ping time (in nanoseconds) to the server
	 */
	public ServerData(final String hostName, final String serverName, final int players, final boolean inGame, final long ping)
	{
		this.hostName = hostName;
		this.serverName = serverName;
		this.players = players;
		this.inGame = inGame;
		this.ping = ping;
	}
}

package com.evervoid.client.discovery;

public class ServerData
{
	public final String hostName;
	public final boolean inGame;
	public final double ping;
	public final int players;
	public final String serverName;

	public ServerData(final String hostName, final String serverName, final int players, final boolean inGame, final double ping)
	{
		this.hostName = hostName;
		this.serverName = serverName;
		this.players = players;
		this.inGame = inGame;
		this.ping = ping;
	}
}

package com.evervoid.client.discovery;

public interface ServerDiscoveryObserver
{
	public void noServersFound();

	public void resetFoundServers();

	public void serverFound(ServerData server);
}

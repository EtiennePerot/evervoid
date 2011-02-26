package com.evervoid.client.discovery;

public interface ServerDiscoveryObserver
{
	public void resetFoundServers();

	public void serverFound(ServerData server);
}

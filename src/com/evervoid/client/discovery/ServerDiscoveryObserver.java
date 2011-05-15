package com.evervoid.client.discovery;

/**
 * Classes that wish to listen for server discovery events should implement this interface.
 */
public interface ServerDiscoveryObserver
{
	/**
	 * Called when the search for servers hasn't been fruitful.
	 */
	public void noServersFound();

	/**
	 * Called when a refresh() has been requested.
	 */
	public void resetFoundServers();

	/**
	 * Called when a single server has been discovered
	 * 
	 * @param server
	 *            The information about the requested server.
	 */
	public void serverFound(ServerData server);
}

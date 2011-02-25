package com.evervoid.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import com.jme3.network.connection.Client;

public class EVServerDiscovery
{
	public static List<String> discoverServers()
	{
		return discoverServers(51255, 51255);
	}

	public static List<String> discoverServers(final int pTCPport, final int pUDPport)
	{
		final Client tmpClient = new Client();
		try {
			final List<InetAddress> found = tmpClient.discoverHosts(pUDPport, 1000);
			final List<String> hosts = new ArrayList<String>(found.size());
			for (final InetAddress addr : found) {
				hosts.add(addr.getCanonicalHostName());
			}
			return hosts;
		}
		catch (final IOException e) {
			return new ArrayList<String>();
		}
	}
}

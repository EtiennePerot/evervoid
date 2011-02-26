package com.evervoid.client.discovery;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evervoid.json.Json;
import com.evervoid.network.EverMessage;
import com.evervoid.network.EverMessageHandler;
import com.evervoid.network.EverMessageListener;
import com.evervoid.network.RequestServerInfo;
import com.jme3.network.connection.Client;

public class EVDiscoveryService implements EverMessageListener
{
	private static final Logger sDiscoveryLog = Logger.getLogger(EVDiscoveryService.class.getName());
	private static BlockingQueue<ServerDiscoveryObserver> sObservers = new LinkedBlockingQueue<ServerDiscoveryObserver>();
	private static Map<String, EVDiscoveryService> sPingServices = new HashMap<String, EVDiscoveryService>();

	public static void addObserver(final ServerDiscoveryObserver observer)
	{
		sObservers.add(observer);
	}

	private static void foundServer(final ServerData data)
	{
		for (final ServerDiscoveryObserver observer : sObservers) {
			observer.serverFound(data);
		}
	}

	public static void refresh()
	{
		sDiscoveryLog.setLevel(Level.ALL);
		sDiscoveryLog.info("Refreshing discovered servers.");
		final Client tmpClient = new Client();
		try {
			final List<InetAddress> found = tmpClient.discoverHosts(51255, 1000);
			for (final InetAddress addr : found) {
				sDiscoveryLog.info("Pinging server: " + addr);
				sendPing(addr.getCanonicalHostName());
			}
		}
		catch (final IOException e) {
			sDiscoveryLog.info("Caught IOException while discovering servers.");
			e.printStackTrace();
		}
		sDiscoveryLog.info("End of server discovery.");
	}

	private static void sendPing(final String ip)
	{
		if (!sPingServices.containsKey(ip)) {
			final EVDiscoveryService service = new EVDiscoveryService(ip);
			sPingServices.put(ip, service);
			(new Thread()
			{
				@Override
				public void run()
				{
					service.ping();
				}
			}).start();
		}
	}

	private Client aClient = null;
	private final String aHostname;
	private long aNanos;

	private EVDiscoveryService(final String ip)
	{
		sDiscoveryLog.info("Initialize discovery subservice for IP: " + ip);
		aHostname = ip;
	}

	private void destroy()
	{
		sPingServices.remove(aHostname);
		if (aClient != null) {
			try {
				aClient.disconnect();
			}
			catch (final IOException e) {
				// Too bad, again
			}
			aClient = null;
		}
	}

	@Override
	public void messageReceived(final EverMessage message)
	{
		sDiscoveryLog.info("Received server info from: " + aHostname);
		final String type = message.getType();
		if (type.equals("serverinfo")) {
			final long ping = System.nanoTime() - aNanos;
			final Json contents = message.getJson();
			foundServer(new ServerData(aHostname, contents.getStringAttribute("name"), contents.getIntAttribute("players"),
					contents.getBooleanAttribute("ingame"), ping));
		}
		destroy();
	}

	private void ping()
	{
		sDiscoveryLog.info("Pinging discovered server at: " + aHostname);
		aClient = new Client();
		final EverMessageHandler handler = new EverMessageHandler(aClient);
		handler.addMessageListener(this);
		aNanos = System.nanoTime();
		try {
			aClient.connect("localhost", 51255, 51255);
			aClient.start();
			Thread.sleep(500);
		}
		catch (final Exception e) {
			sDiscoveryLog.info("Caught exception while pinging server at: " + aHostname);
			e.printStackTrace();
			destroy(); // Too bad
		}
		handler.send(new RequestServerInfo());
	}
}

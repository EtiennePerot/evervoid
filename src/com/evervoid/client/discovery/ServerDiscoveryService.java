package com.evervoid.client.discovery;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.evervoid.client.EVViewManager;
import com.evervoid.json.Json;
import com.evervoid.network.EverMessage;
import com.evervoid.network.EverMessageHandler;
import com.evervoid.network.EverMessageListener;
import com.evervoid.network.RequestServerInfo;
import com.evervoid.network.ServerInfoMessage;
import com.evervoid.server.EverVoidServer;
import com.evervoid.utils.LoggerUtils;
import com.jme3.network.connection.Client;

/**
 * The main class of the discovery service package. Provides a static facade to other classes to request information from the
 * server discovery service. Non-static methods and attributes correspond to worker-specific information.
 */
public class ServerDiscoveryService implements EverMessageListener
{
	/**
	 * Queue of observers to notify whenever a server is found
	 */
	private static BlockingQueue<ServerDiscoveryObserver> sObservers = new LinkedBlockingQueue<ServerDiscoveryObserver>();
	/**
	 * Maps Inet addresses to discovery "worker" threads
	 */
	private static Map<String, ServerDiscoveryService> sPingServices = new HashMap<String, ServerDiscoveryService>();
	/**
	 * Delay between {moment the connection is established} and {moment to send server information request}
	 */
	private static final long sWaitBeforeRequest = 100;

	/**
	 * Add a server discovery observer to notify.
	 * 
	 * @param observer
	 *            The observer to notify.
	 */
	public static void addObserver(final ServerDiscoveryObserver observer)
	{
		sObservers.add(observer);
	}

	/**
	 * Main host discovery loop. Creates worker threads to discover available servers.
	 */
	private static void discoverHosts()
	{
		LoggerUtils.info("Refreshing discovered servers.");
		final Client tmpClient = new Client();
		try {
			final List<InetAddress> found = tmpClient.discoverHosts(EverVoidServer.sDiscoveryPortUDP, 1000);
			for (final InetAddress addr : found) {
				LoggerUtils.info("Pinging server: " + addr);
				sendRequest(addr.getHostAddress());
			}
			if (found.isEmpty()) {
				LoggerUtils.info("Discovery service has found no servers.");
				EVViewManager.schedule(new Runnable()
				{
					@Override
					public void run()
					{
						for (final ServerDiscoveryObserver observer : sObservers) {
							observer.noServersFound();
						}
					}
				});
			}
		}
		catch (final IOException e) {
			LoggerUtils.info("Caught IOException while discovering servers.");
			e.printStackTrace();
		}
		LoggerUtils.info("End of server discovery.");
	}

	/**
	 * Called whenever a single server is found. Notifies all observers on the UI thread.
	 * 
	 * @param data
	 *            The data corresponding to the server found.
	 */
	private static void foundServer(final ServerData data)
	{
		EVViewManager.schedule(new Runnable()
		{
			@Override
			public void run()
			{
				for (final ServerDiscoveryObserver observer : sObservers) {
					observer.serverFound(data);
				}
			}
		});
	}

	/**
	 * Refreshes the list of available servers. External classes should call this method whenever the user requests a refresh.
	 * Thread-safe.
	 */
	public static void refresh()
	{
		EVViewManager.schedule(new Runnable()
		{
			@Override
			public void run()
			{
				for (final ServerDiscoveryObserver observer : sObservers) {
					observer.resetFoundServers();
				}
			}
		});
		(new Thread()
		{
			@Override
			public void run()
			{
				discoverHosts();
			}
		}).start();
	}

	/**
	 * Sends a server info request to a single server
	 * 
	 * @param ip
	 *            The address of the server to send the request to
	 */
	private static void sendRequest(final String ip)
	{
		if (!sPingServices.containsKey(ip)) {
			final ServerDiscoveryService service = new ServerDiscoveryService(ip);
			sPingServices.put(ip, service);
			(new Thread()
			{
				@Override
				public void run()
				{
					service.request();
				}
			}).start();
		}
	}

	/**
	 * Reference to the jMonkeyEngine Client object used to connect to the server
	 */
	private Client aClient = null;
	/**
	 * Hostname (address) of the server
	 */
	private final String aHostname;
	/**
	 * Time (in nanoseconds) at which the connection request was sent
	 */
	private long aNanos;

	/**
	 * Initializes a server discovery worker
	 * 
	 * @param ip
	 *            The address of the server that the worker should take care of
	 */
	private ServerDiscoveryService(final String ip)
	{
		aHostname = ip;
		LoggerUtils.info("Initializing discovery subservice for IP: " + aHostname);
	}

	/**
	 * Clean up and disconnect the worker when it is not needed anymore.
	 */
	private void destroy()
	{
		LoggerUtils.info("Destroying discovery subservice for IP: " + aHostname);
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
		LoggerUtils.info("Received server info from: " + aHostname);
		final String type = message.getType();
		if (type.equals(ServerInfoMessage.class.getName())) {
			final long ping = System.nanoTime() - aNanos - sWaitBeforeRequest * 1000000;
			final Json contents = message.getJson();
			foundServer(new ServerData(aHostname, contents.getStringAttribute("name"), contents.getIntAttribute("players"),
					contents.getBooleanAttribute("ingame"), ping));
		}
		destroy();
	}

	/**
	 * Do the actual request to the server. Try to connect, and send the info request.
	 */
	private void request()
	{
		LoggerUtils.info("Pinging discovered server at: " + aHostname);
		aClient = new Client();
		final EverMessageHandler handler = new EverMessageHandler(aClient);
		handler.addMessageListener(this);
		aNanos = System.nanoTime();
		try {
			aClient.connect(aHostname, EverVoidServer.sDiscoveryPortTCP, EverVoidServer.sDiscoveryPortUDP);
			aClient.start();
			Thread.sleep(sWaitBeforeRequest);
			handler.send(new RequestServerInfo());
		}
		catch (final Exception e) {
			LoggerUtils.info("Caught exception while pinging server at: " + aHostname);
			e.printStackTrace();
			destroy(); // Too bad
		}
	}
}

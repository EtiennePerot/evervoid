package com.evervoid.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evervoid.json.Json;
import com.evervoid.network.ChatMessage;
import com.evervoid.network.EverMessage;
import com.evervoid.network.EverMessageHandler;
import com.evervoid.network.EverMessageListener;
import com.evervoid.network.ServerChatMessage;
import com.evervoid.network.lobby.LobbyPlayer;
import com.evervoid.network.lobby.LobbyState;
import com.evervoid.network.lobby.LobbyStateMessage;
import com.evervoid.state.data.GameData;
import com.jme3.network.connection.Client;
import com.jme3.network.connection.Server;
import com.jme3.network.events.ConnectionListener;

/**
 * everVoid Server allowing communication from and to clients.
 */
public class EVServerEngine implements ConnectionListener, EverMessageListener
{
	private static EVServerEngine sInstance = null;
	public static final Logger sServerLog = Logger.getLogger(EVServerEngine.class.getName());
	private static String[] sValidLobbyMessages = { "handshake", "lobbyplayer" };

	public static EVServerEngine getInstance()
	{
		if (sInstance == null) {
			sInstance = new EVServerEngine();
		}
		return sInstance;
	}

	public static void registerListener(final EVServerMessageObserver listener)
	{
		sInstance.aObservers.add(listener);
	}

	private boolean aInGame = false;
	private final LobbyState aLobby;
	private final EverMessageHandler aMessageHandler;
	public final Set<EVServerMessageObserver> aObservers;
	private Server aSpiderMonkeyServer;
	private final int aTCPport;
	private final int aUDPport;

	/**
	 * Constructor for the EverVoidServer using default ports.
	 */
	private EVServerEngine()
	{
		this(51255, 51255);
	}

	/**
	 * Constructor with specified UDP and TCP ports.
	 * 
	 * @param pTCPport
	 *            TCP port to use.
	 * @param pUDPport
	 *            UDP port to use.
	 */
	private EVServerEngine(final int pTCPport, final int pUDPport)
	{
		sInstance = this;
		aObservers = new HashSet<EVServerMessageObserver>();
		// The game data is loaded from the default JSON file here; might want to load it from the real game state, but they
		// should match anyway
		aLobby = new LobbyState(new GameData(), "My cool everVoid server");
		sServerLog.setLevel(Level.ALL);
		sServerLog.info("Creating server on ports " + pTCPport + "; " + pUDPport);
		aTCPport = pTCPport;
		aUDPport = pUDPport;
		try {
			aSpiderMonkeyServer = new Server(aTCPport, aUDPport);
		}
		catch (final IOException e) {
			sServerLog.severe("Could not initialise the server. Caught IOException.");
		}
		sServerLog.info("Server created: " + aSpiderMonkeyServer);
		aMessageHandler = new EverMessageHandler(aSpiderMonkeyServer);
		aMessageHandler.addMessageListener(this);
		aSpiderMonkeyServer.addConnectionListener(this);
		sServerLog.info("Set connection listener and message listener.");
		try {
			aSpiderMonkeyServer.start();
		}
		catch (final IOException e) {
			sServerLog.info("Cannot start server: " + e.getStackTrace());
		}
		sServerLog.info("Server up and waiting for connections.");
	}

	@Override
	public void clientConnected(final Client client)
	{
		sServerLog.info("Client connected: " + client);
	}

	@Override
	public void clientDisconnected(final Client client)
	{
		sServerLog.info("Client disconnected: " + client);
		aLobby.removePlayer(client);
		refreshLobbies();
	}

	public void deregisterObserver(final EVServerMessageObserver observer)
	{
		aObservers.remove(observer);
	}

	/**
	 * Handles an EverMessage
	 * 
	 * @param message
	 *            An EverMessage; can be a lobby or a non-lobby one
	 * @return True if the message is a lobby one and was handled, false otherwise
	 */
	private boolean handleLobbyMessage(final EverMessage message)
	{
		final String messageType = message.getType();
		final Json content = message.getJson();
		if (messageType.equals("handshake")) {
			if (aLobby.getPlayerByClient(message.getClient()) != null || aInGame) {
				// Some guy is trying to handshake twice or handshaking in-game -> DENIED
				return true;
			}
			final String nickname = content.getStringAttribute("nickname");
			aLobby.addPlayer(message.getClient(), nickname);
			refreshLobbies();
			return true;
		}
		// It's not a handshake, so it must be a legit lobby message from this point on
		if (!isLegitLobbyMessage(message)) {
			// DENIED
			return true;
		}
		if (messageType.equals("lobbyplayer")) {
			if (aLobby.updatePlayer(message.getClient(), content)) {
				refreshLobbies();
			}
		}
		return false;
	}

	/**
	 * Checks whether this message is is sent during the lobby phase by a valid client
	 * 
	 * @param message
	 *            The EverMessage to check
	 * @return True if {Client is authenticated to the lobby} AND {Game has not started}
	 */
	private boolean isLegitLobbyMessage(final EverMessage message)
	{
		return !aInGame && aLobby.getPlayerByClient(message.getClient()) != null;
	}

	@Override
	public void messageReceived(final EverMessage message)
	{
		if (handleLobbyMessage(message)) {
			// If it's a lobby message, intercept it and don't send it to the observers
			return;
		}
		final String messageType = message.getType();
		Json messageContents = message.getJson();
		if (messageType.equals("chat")) {
			final LobbyPlayer fromPlayer = aLobby.getPlayerByClient(message.getClient());
			sendAll(new ChatMessage(fromPlayer.getNickname(), fromPlayer.getColor(),
					messageContents.getStringAttribute("message")));
		}
		else if (messageType.equals("startgame")) {
			if (!readyToStart()) {
				send(message.getClient(), new ServerChatMessage("Cannot start game yet!"));
				return;
			}
			messageContents = new Json(aLobby.getPlayers());
			aInGame = true;
		}
		for (final EVServerMessageObserver observer : aObservers) {
			observer.messageReceived(messageType, message.getClient(), messageContents);
		}
	}

	/**
	 * @return Whether we can start the game right now
	 */
	private boolean readyToStart()
	{
		for (final LobbyPlayer player : aLobby.getPlayers()) {
			if (!player.isReady()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Send a message to all clients containing the current lobby info
	 */
	private void refreshLobbies()
	{
		sendAll(new LobbyStateMessage(aLobby));
	}

	public void send(final Client client, final EverMessage message)
	{
		aMessageHandler.send(client, message);
	}

	public void sendAll(final EverMessage message)
	{
		for (final LobbyPlayer player : aLobby) {
			send(player.getClient(), message);
		}
	}

	public void stop()
	{
		try {
			aSpiderMonkeyServer.stop();
		}
		catch (final IOException e) {
			sServerLog.severe("Could not stop the server. Caught IOException.");
		}
	}
}

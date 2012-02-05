package com.evervoid.server;

import static com.evervoid.network.EVNetworkServer.sDiscoveryPortTCP;
import static com.evervoid.network.EVNetworkServer.sDiscoveryPortUDP;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.evervoid.json.BadJsonInitialization;
import com.evervoid.json.Json;
import com.evervoid.network.EVMessage;
import com.evervoid.network.EVMessageListener;
import com.evervoid.network.EVMessageSendingException;
import com.evervoid.network.EVNetworkServer;
import com.evervoid.network.lobby.LobbyPlayer;
import com.evervoid.network.lobby.LobbyState;
import com.evervoid.network.message.ChatMessage;
import com.evervoid.network.message.GameStateMessage;
import com.evervoid.network.message.JoinErrorMessage;
import com.evervoid.network.message.LobbyStateMessage;
import com.evervoid.network.message.PingMessage;
import com.evervoid.network.message.ServerChatMessage;
import com.evervoid.network.message.ServerInfoMessage;
import com.evervoid.network.message.ServerQuitting;
import com.evervoid.network.message.StartingGameMessage;
import com.evervoid.network.message.lobby.LeavingLobby;
import com.evervoid.network.message.lobby.LoadGameRequest;
import com.evervoid.network.message.lobby.LobbyMessage;
import com.evervoid.network.message.lobby.LobbyPlayerUpdate;
import com.evervoid.network.message.lobby.RequestJoinLobby;
import com.evervoid.network.message.lobby.RequestServerInfo;
import com.evervoid.network.message.lobby.StartGameMessage;
import com.evervoid.state.BadSaveFileException;
import com.evervoid.state.EVGameState;
import com.evervoid.state.data.GameData;
import com.evervoid.state.player.Player;
import com.evervoid.utils.LoggerUtils;
import com.evervoid.utils.MathUtils;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.MessageConnection;
import com.jme3.network.Server;

/**
 * everVoid Server allowing communication from and to clients.
 */
public class EVNetworkEngine implements EVMessageListener, ConnectionListener
{
    /**
     * The time between two pings
     */
    public static final int sTimeBetweenPings = 15000;
    /**
     * The types of messages relate to the lobby.
     */
    private static String[] sValidLobbyMessages = { RequestServerInfo.class.getName(),
            RequestJoinLobby.class.getName(), LobbyPlayerUpdate.class.getName(), StartGameMessage.class.getName(),
            LoadGameRequest.class.getName(), LeavingLobby.class.getName() };
    /**
     * A dummy server that allows players to ping for discovery.
     */
    private EVNetworkServer aDiscoveryServer;
    /**
     * All observers waiting for game messages.
     */
    private final Set<EVGameMessageObserver> aGameMessagesObservers;
    /**
     * Whether the game is running
     */
    private boolean aInGame = false;
    /**
     * The current state of the lobby.
     */
    private LobbyState aLobby;
    /**
     * The ping time of the slowest client in any given round of pinging.
     */
    private long aLongestPingTime;
    /**
     * The server for all players connected to the game.
     */
    private EVNetworkServer aNetworkServer;
    /**
     * The timer that runs pinging rounds.
     */
    private Timer aPingTimer;

    /**
     * Constructor for the EverVoidServer using default ports.
     */
    public EVNetworkEngine()
    {
        aGameMessagesObservers = new HashSet<EVGameMessageObserver>();
        // The game data is loaded from the default JSON file here; might want to load it from the real game state, but they
        // should match anyway
        try {
            aLobby = new LobbyState(new GameData(), "My cool everVoid server");
        } catch (final BadJsonInitialization e2) {
            LoggerUtils.info("Cannot read game data: " + e2.getStackTrace());
            aLobby = null;
        }
        LoggerUtils.info("Creating server on ports " + sDiscoveryPortTCP + "; " + sDiscoveryPortUDP);
        try {
            aNetworkServer = new EVNetworkServer();
        } catch (final Exception e) {
            LoggerUtils.severe("Could not initialise the server. Caught IOException.");
            e.printStackTrace();
            return;
        }
        try {
            aDiscoveryServer = new EVNetworkServer(sDiscoveryPortTCP, sDiscoveryPortUDP);
            aDiscoveryServer.addEVMessageListener(this);
        } catch (final Exception e) {
            LoggerUtils.warning("Could not initialise discovery server. Caught IOException.");
            // No big deal, just won't be available for discovery
        }
        LoggerUtils.info("Server created: " + aNetworkServer);
        aNetworkServer.addEVMessageListener(this);
        aNetworkServer.addConnectionListener(this);
        LoggerUtils.info("Set connection listener and message listener, initializing game engine.");
        try {
            new EVGameEngine(this); // Will register itself (bad?)
        } catch (final BadJsonInitialization e1) {
            LoggerUtils.info("Cannot start game engine: " + e1.getStackTrace());
        }
        try {
            aNetworkServer.start();
        } catch (final Exception e) {
            LoggerUtils.info("Cannot start server: " + e.getStackTrace());
        }
        try {
            aDiscoveryServer.start();
        } catch (final Exception e) {
            LoggerUtils.info("Cannot start discovery server: " + e.getStackTrace());
        }
        LoggerUtils.info("Server up and waiting for connections.");
    }

    @Override
    public void connectionAdded(final Server server, final HostedConnection conn)
    {}

    @Override
    public void connectionRemoved(final Server server, final HostedConnection conn)
    {}

    /**
     * unregisters an observer from the network engine.
     * 
     * @param observer
     *            The observer to unregister.
     */
    public void deregisterObserver(final EVGameMessageObserver observer)
    {
        aGameMessagesObservers.remove(observer);
    }

    /**
     * @return A random player nickname.
     */
    public String getNewPlayerNickame()
    {
        final Json names = Json.fromFile("schema/players.json");
        final List<String> freeNames = new ArrayList<String>();
        for (final String name : names.getStringListAttribute("names")) {
            if (aLobby.getPlayerByNickname(name) == null) {
                freeNames.add(name);
            }
        }
        if (!freeNames.isEmpty()) {
            return MathUtils.getRandomElement(freeNames);
        }
        // Otherwise, generate a boring name
        int i;
        for (i = 1; aLobby.getPlayerByNickname("Player " + i) != null; i++) {
            // Nothing
        }
        return "Player " + i;
    }

    /**
     * Handles an EverMessage
     * 
     * @param source
     *            The source of the lobby message.
     * @param message
     *            An EverMessage; can be a lobby or a non-lobby one
     * @return True if the message is a lobby one and was handled, false otherwise
     */
    private boolean handleLobbyMessage(final HostedConnection source, final EVMessage message)
    {
        final String messageType = message.getType();
        // Handle only lobby messages
        final boolean isLobby = false;
        if (!(message instanceof LobbyMessage)) {
            return false;
        }
        /*
         * for (final String s : sValidLobbyMessages) {
         * if (s.equals(messageType)) {
         * isLobby = true;
         * }
         * }
         * if (!isLobby) { return false; }
         */

        final Json content = message.getContent();
        if (messageType.equals(RequestServerInfo.class.getName())) {
            try {
                aDiscoveryServer.sendEVMessage(source, new ServerInfoMessage(aLobby, aInGame), true);
            } catch (final EVMessageSendingException e) {
                // No big deal, client just won't see us in server list
            }
            aLobby.removePlayer(source); // If it was in the lobby somehow, remove it
            return true;
        }
        if (messageType.equals(RequestJoinLobby.class.getName())) {
            if (aLobby.getPlayerByClient(source) != null || aInGame) {
                // Some guy is trying to handshake twice or handshaking in-game -> DENIED
                return true;
            }
            String nickname = content.getStringAttribute("nickname");
            if (nickname.equals(EVGameState.sNeutralPlayerName)) {
                sendEVMessage(source, new JoinErrorMessage("Nickname \"" + EVGameState.sNeutralPlayerName
                                + "\" is reserved."));
                return true;
            }
            if (aLobby.getPlayerByNickname(nickname) != null) {
                // Nickname already in use
                nickname = getNewPlayerNickame();
            }
            LoggerUtils.info("Adding player " + nickname + " at Client " + source + " to lobby.");
            aLobby.addPlayer(source, nickname);
            refreshLobbies();
            return true;
        } else if (messageType.equals(LeavingLobby.class.getName())) {
            aLobby.removePlayer(source);
            refreshLobbies();
            return true;
        }
        // It's not a handshake, so it must be a legit lobby message from this point on
        if (aInGame || aLobby.getPlayerByClient(source) == null) {
            return true; // We're in-game or client is not authenticated
        }
        if (messageType.equals(LobbyPlayerUpdate.class.getName())) {
            if (aLobby.updatePlayer(source, content)) {
                refreshLobbies();
            }
        } else if (messageType.equals(StartGameMessage.class.getName())) {
            if (!isReadyToStart()) {
                sendEVMessage(source, new ServerChatMessage("Cannot start game yet, some players are not ready."));
            } else {
                // Starting game! Build list of lobby players and pass it to game observers
                sendAll(new ServerChatMessage("Game starting."));
                sendAll(new StartingGameMessage());
                aInGame = true;
                final Json players = aLobby.getBaseJson();
                for (final EVGameMessageObserver observer : aGameMessagesObservers) {
                    observer.messageReceived(messageType, aLobby, source, players);
                }
                // ping all to get a feel for ping times
                pingAll();
            }
        } else if (messageType.equals(LoadGameRequest.class.getName())) {
            EVGameState loaded;
            LoggerUtils.info("Attempting to load game from Client " + source + ".");
            try {
                loaded = loadGame(content);
                // Start game, no errors
                sendAll(new ServerChatMessage("Loaded game starting."));
                sendAll(new StartingGameMessage());
                aInGame = true;
                LoggerUtils.info("Successfully loaded game from Client " + source + ".");
                for (final EVGameMessageObserver observer : aGameMessagesObservers) {
                    observer.messageReceived(messageType, aLobby, source, loaded.toJson());
                }
            } catch (final BadSaveFileException e) {
                LoggerUtils.info("Eror while loading game from Client " + source + ": " + e.getMessage());
                sendAll(new ServerChatMessage("Error while loading game: " + e.getMessage()));
            }
        }
        return true;
    }

    /**
     * @return Whether the game is running.
     */
    public boolean isGameRunning()
    {
        return aInGame;
    }

    /**
     * @return Whether we can start the game right now
     */
    private boolean isReadyToStart()
    {
        for (final LobbyPlayer player : aLobby.getPlayers()) {
            if (!player.isReady()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Attempt to load a game from a save file
     * 
     * @param state
     *            The Json'd game state to load
     * @return The loaded game state
     * @throws BadSaveFileException
     *             When an error happens during loading.
     */
    private EVGameState loadGame(final Json state) throws BadSaveFileException
    {
        EVGameState loadedState;
        try {
            loadedState = new EVGameState(state);
        } catch (final BadJsonInitialization e) {
            throw new BadSaveFileException("Invalid save file.");
        }
        // First, match all players; don't modify them yet until we're sure we have everyone
        String missingPlayers = "";
        int missingCount = 0;
        for (final Player p : loadedState.getPlayers()) {
            if (p.equals(loadedState.getNullPlayer())) {
                continue;
            }
            final LobbyPlayer lobbyP = aLobby.getPlayerByNickname(p.getNickname());
            if (lobbyP == null) {
                missingPlayers += ", " + p.getNickname();
                missingCount++;
            }
        }
        if (missingCount != 0) {
            throw new BadSaveFileException("Missing player" + (missingCount == 1 ? "" : "s") + ": "
                            + missingPlayers.substring(2) + ".");
        }
        // Second, check if everyone is ready
        if (!isReadyToStart()) {
            throw new BadSaveFileException("Players not ready.");
        }
        // Third, modify players to match loaded state
        for (final Player p : loadedState.getPlayers()) {
            if (p.equals(loadedState.getNullPlayer())) {
                continue;
            }
            final LobbyPlayer lobbyP = aLobby.getPlayerByNickname(p.getNickname());
            lobbyP.setColor(p.getColor().name);
            lobbyP.setRace(p.getRaceData().getType());
        }
        // All good, start the damn game already
        return loadedState;
    }

    /**
     * @return The maximum amount of time we wait before pinging a silent connection.
     */
    public double maxPingTime()
    {
        return aLongestPingTime;
    }

    @Override
    public void messageReceived(final MessageConnection source, final EVMessage message)
    {
        final HostedConnection hc = (HostedConnection) source;
        final String messageType = message.getType();
        final Json messageContents = message.getContent();
        if (messageType.equals(PingMessage.class.getName())) {
            // it's just a ping, set maxPingTime if you must
            final long timeDiff = System.currentTimeMillis() - messageContents.getLong();
            aLongestPingTime = Math.max(aLongestPingTime, timeDiff);
            return;
        }
        // Handle global messages first
        if (messageType.equals(ChatMessage.class.getName())) {
            final LobbyPlayer fromPlayer = aLobby.getPlayerByClient(hc);
            sendAll(new ChatMessage(fromPlayer.getNickname(), fromPlayer.getColor(),
                            messageContents.getStringAttribute("message")));
            return;
        }
        // Else, handle lobby messages
        if (handleLobbyMessage(hc, message)) {
            // If it's a lobby message, intercept it and don't send it to the observers
            return;
        }
        // Else, it's a game message, so forward to game observers
        for (final EVGameMessageObserver observer : aGameMessagesObservers) {
            observer.messageReceived(messageType, aLobby, hc, messageContents);
        }
    }

    /**
     * Pings the connection.
     * 
     * @param client
     *            The connection to ping.
     */
    private void ping(final HostedConnection client)
    {
        sendEVMessage(client, new PingMessage());
    }

    /**
     * Pings all players.
     */
    public void pingAll()
    {
        LoggerUtils.info("Previous max ping time was " + aLongestPingTime + ". Starting another round of pinging");
        // reset max ping time so that it's up do date
        aLongestPingTime = 0;
        // ping all players
        for (final LobbyPlayer player : new ArrayList<LobbyPlayer>(aLobby.getPlayers())) {
            ping(player.getClient());
        }
        // schedule a new round of pinging
        if (aPingTimer != null) {
            aPingTimer.cancel();
        }
        aPingTimer = new Timer();
        aPingTimer.schedule(new TimerTask() {
            @Override
            public void run()
            {
                pingAll();
            }
        }, sTimeBetweenPings);
        // ping every once in a while
    }

    /**
     * Send a message to all clients containing the current lobby info
     */
    private void refreshLobbies()
    {
        for (final LobbyPlayer lobbyplayer : new ArrayList<LobbyPlayer>(aLobby.getPlayers())) {
            sendEVMessage(lobbyplayer.getClient(), new LobbyStateMessage(aLobby, lobbyplayer));
        }
    }

    /**
     * Registers a listener for all EVMessages.
     * 
     * @param listener
     *            The listener.
     */
    public void registerListener(final EVGameMessageObserver listener)
    {
        aGameMessagesObservers.add(listener);
    }

    /**
     * Sends the message to all players.
     * 
     * @param message
     *            The message to send.
     */
    protected void sendAll(final EVMessage message)
    {
        // clone list to deal with potential concurrent modification
        for (final LobbyPlayer player : new ArrayList<LobbyPlayer>(aLobby.getPlayers())) {
            sendEVMessage(player.getClient(), message);
        }
    }

    /**
     * Sends the state to all players.
     * 
     * @param state
     *            The state to send.
     */
    void sendAllState(final EVGameState state)
    {
        for (final LobbyPlayer player : new ArrayList<LobbyPlayer>(aLobby.getPlayers())) {
            sendEVMessage(player.getClient(), new GameStateMessage(state, player.getNickname()));
        }
    }

    /**
     * Sends an EVMessage asynchronously to a connected client.
     * 
     * @param destination
     *            The destination of the message.
     * @param message
     *            The message to send.
     */
    protected void sendEVMessage(final HostedConnection destination, final EVMessage message)
    {
        sendEVMessage(destination, message, true);
    }

    /**
     * Sends an EVMessage to a connected client.
     * 
     * @param destination
     *            The destination of the message.
     * @param message
     *            The message to send.
     * @param async
     *            whether to send the message asynchronously.
     */
    protected void sendEVMessage(final HostedConnection destination, final EVMessage message, final boolean async)
    {
        try {
            aNetworkServer.sendEVMessage(destination, message, async);
        } catch (final Exception e) {
            LoggerUtils.severe("Could not send message " + message + " to client " + destination);
            e.printStackTrace();
            destination.close("unresponsive");
            aLobby.removePlayer(destination);
            refreshLobbies();
        }
    }

    /**
     * Stops the network engine if it running.
     */
    void stop()
    {
        // 1. we are no longer in a game
        aInGame = false;
        // 2. if there's a ping timer floating around, kill it
        // do this first otherwise ping messages might be sent to connections we're currently attempting to close
        if (aPingTimer != null) {
            aPingTimer.cancel();
            aPingTimer = null;
        }
        // 3. for each connected client send a quit message synchronously then close the connection
        for (final HostedConnection client : new ArrayList<HostedConnection>(aNetworkServer.getConnections())) {
            try {
                if (client != null) {
                    // needs to be done synchronously otherwise the connection may close before we try to send and raise a NPE
                    sendEVMessage(client, new ServerQuitting(), false);
                    client.close("Server stopping, quitting clients");
                }
            } catch (final Exception e) {
                LoggerUtils.warning("Could not kick client " + client);
            }
        }
        // 4. inform all observers that the server has just stopped
        for (final EVGameMessageObserver observer : aGameMessagesObservers) {
            observer.serverStopped();
        }
        // 5. shut down the discovery server, we don't want people trying to join our game if it doesn't exist
        try {
            if (aDiscoveryServer.isRunning()) {
                aDiscoveryServer.close();
            }
        } catch (final Exception e) {
            LoggerUtils.warning("Could not stop discovery server. Caught " + e.getClass().getName());
            e.printStackTrace();
            System.exit(0);
        }
        // 6. shut down the server
        try {
            if (aNetworkServer.isRunning()) {
                aNetworkServer.close();
            }
        } catch (final Exception e) {
            LoggerUtils.severe("Could not stop the server. Caught " + e.getClass().getName());
            e.printStackTrace();
            System.exit(0);
        }
        try {
            Thread.sleep(500); // Server needs some time to actually unbind
        } catch (final InterruptedException e) {
            // should never happen
        }
        LoggerUtils.info("The game server has succesfully shut down");
    }
}

package com.evervoid.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evervoid.json.BadJsonInitialization;
import com.evervoid.json.Json;
import com.evervoid.network.lobby.LobbyPlayer;
import com.evervoid.network.lobby.LobbyState;
import com.evervoid.network.message.LoadGameRequest;
import com.evervoid.network.message.PlayerDefeatedMessage;
import com.evervoid.network.message.PlayerVictoryMessage;
import com.evervoid.network.message.ReadyMessage;
import com.evervoid.network.message.RequestGameState;
import com.evervoid.network.message.SaveGameStateReply;
import com.evervoid.network.message.ServerChatMessage;
import com.evervoid.network.message.StartGameMessage;
import com.evervoid.network.message.TurnMessage;
import com.evervoid.state.EVGameState;
import com.evervoid.state.action.Action;
import com.evervoid.state.action.IllegalEVActionException;
import com.evervoid.state.action.Turn;
import com.evervoid.state.action.building.IncrementBuildingConstruction;
import com.evervoid.state.action.building.IncrementShipConstruction;
import com.evervoid.state.action.planet.RegeneratePlanet;
import com.evervoid.state.action.player.ReceiveIncome;
import com.evervoid.state.action.ship.BombPlanet;
import com.evervoid.state.action.ship.CapturePlanet;
import com.evervoid.state.action.ship.EnterCargo;
import com.evervoid.state.action.ship.JumpShipIntoPortal;
import com.evervoid.state.action.ship.LeaveCargo;
import com.evervoid.state.action.ship.MoveShip;
import com.evervoid.state.action.ship.RegenerateShip;
import com.evervoid.state.action.ship.ShootShip;
import com.evervoid.state.data.GameData;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Ship;
import com.jme3.network.HostedConnection;

/**
 * EVGameEngine runs an everVoid game by making sure rules are enforced, compiling turns, managing players, dealing with
 * timeouts....
 */
public class EVGameEngine implements EVGameMessageObserver
{
	/**
	 * The GameEngine logs.
	 */
	private static final Logger aGameEngineLog = Logger.getLogger(EVGameEngine.class.getName());
	/**
	 * A list of all {@link Action} classes that have to do with combat.
	 */
	public static final Class<?>[] sCombatActionTypes = { ShootShip.class, BombPlanet.class };
	/**
	 * A list of all {@link Action} classes that have to do with moving.
	 */
	public static final Class<?>[] sMoveActionTypes = { MoveShip.class, JumpShipIntoPortal.class };
	/**
	 * A map from Client to Player.
	 */
	private final Map<HostedConnection, Player> aClientMap = new HashMap<HostedConnection, Player>();
	/**
	 * The current game data.
	 */
	private final GameData aGameData;
	/**
	 * A set of all clients in the game.
	 */
	private HashSet<HostedConnection> aReadyMap;
	/**
	 * The Server.
	 */
	protected EVNetworkEngine aServer;
	/**
	 * The current state of the game.
	 */
	private EVGameState aState;
	/**
	 * A map of Players to the set of moves they've handed in.
	 */
	private final Map<Player, Turn> aTurnMap = new HashMap<Player, Turn>();
	/**
	 * The current turn.
	 */
	private int aTurnNumber = 1;
	/**
	 * The timeout timer.
	 */
	private Timer aTurnTimer = new Timer();

	/**
	 * Create a new GameEngine attached to a server.
	 * 
	 * @param server
	 *            The server to which the game is attached.
	 * @throws BadJsonInitialization
	 *             If the default GameData is corrupted.
	 */
	EVGameEngine(final EVNetworkEngine server) throws BadJsonInitialization
	{
		aGameEngineLog.setLevel(Level.ALL);
		aGameEngineLog.info("Game engine starting with server " + server);
		aServer = server;
		server.registerListener(this);
		aGameData = new GameData();
	}

	/**
	 * Adds all the moves from the given turn to the current turn to compile. Makes sure the client is only sending moves
	 * pertaining to themselves.
	 * 
	 * @param client
	 *            The client sending the moves.
	 * @param turn
	 *            The set of moves to add.
	 * @return Whether the moves were added.
	 */
	private boolean addAllMoves(final HostedConnection client, final Turn turn)
	{
		final Player sender = aClientMap.get(client);
		if (sender == null) {
			return false; // Not even a registered player
		}
		for (final Action act : turn) {
			if (!act.getSender().equals(sender)) {
				return false; // Trying to impersonate another player
			}
		}
		if (aTurnMap.get(client) != null) {
			return false;
		}
		aTurnMap.put(sender, turn);
		return true;
	}

	/**
	 * Announces winning party to all players.
	 * 
	 * @param winner
	 *            The winning player.
	 */
	private void announceWinner(final Player winner)
	{
		aServer.sendAll(new PlayerVictoryMessage(winner));
		aServer.sendAll(new ServerChatMessage("Player \"" + winner.getNickname() + "\" has won the game."));
	}

	/**
	 * Calculates the current income for all players.
	 * 
	 * @return A list of actions that will add the correct income to each player.
	 */
	private List<Action> calculateIncome()
	{
		final List<Action> incomeActions = new ArrayList<Action>();
		for (final Player p : aState.getPlayers()) {
			if (p.isNullPlayer()) {
				continue; // no point in giving it income
			}
			try {
				incomeActions.add(new ReceiveIncome(p, aState, p.getCurrentIncome()));
			}
			catch (final IllegalEVActionException e) {
				// hopefully this doesn't happen, we're trying to give players resources
			}
		}
		return incomeActions;
	}

	/**
	 * Adds and moves to the turn and organizes them so they run in the correct order. These are the steps: 1. Ship/Planet
	 * regeneration 2. Combat 3. Docking 4. Unloading 5. Jumping 7. All other movement 8. Building construction 9. Ship
	 * construction 10. Income 11. Check for winning/dropping/...
	 */
	private void calculateTurn()
	{
		// compress all client turns into one
		final Turn combinedTurn = new Turn();
		for (final Player p : aTurnMap.keySet()) {
			combinedTurn.addTurn(aTurnMap.get(p));
		}
		// inform
		aGameEngineLog.info("Game engine building turn from original:\n" + combinedTurn.toJson().toPrettyString());
		// start calculating turn
		// !WARNING!: If you need to modify the order of actions, do not forget to edit res/action_order.txt and
		// TurnSynchronizer to reflect the changes.
		// 1: Regeneration (Ships, planets)
		combinedTurn.reEnqueueActions(regenerateShips());
		combinedTurn.reEnqueueActions(regeneratePlanets());
		// 2: Combat (Shooting, bombing)
		final List<Action> combatActions = combinedTurn.getActionsOfType(sCombatActionTypes);
		for (final Action act : combatActions) {
			if (act instanceof ShootShip) {
				((ShootShip) act).rollDamage();
				combinedTurn.reEnqueueAction(act);
			}
			else if (act instanceof BombPlanet) {
				((BombPlanet) act).rollDamage();
				combinedTurn.reEnqueueAction(act);
			}
		}
		// 3: Docking ships
		shakeUpActions(combinedTurn, EnterCargo.class);
		// 4: Unloading ships
		shakeUpActions(combinedTurn, LeaveCargo.class);
		// 5: Jumps
		shakeUpActions(combinedTurn, JumpShipIntoPortal.class);
		// 6: Capturing planets
		shakeUpActions(combinedTurn, CapturePlanet.class);
		// 7: Movement
		shakeUpActions(combinedTurn, sMoveActionTypes);
		// 8: Building construction
		shakeUpActions(combinedTurn, IncrementBuildingConstruction.class);
		// 9: Ship construction
		shakeUpActions(combinedTurn, IncrementShipConstruction.class);
		// 10: Income
		combinedTurn.reEnqueueActions(calculateIncome());
		// Finally - send out turn
		aServer.sendAll(new TurnMessage(aState.commitTurn(combinedTurn)));
		aTurnNumber++;
		// Check if some other players have lost
		for (final Player p : aState.getPlayers()) {
			if (aState.hasLost(p)) {
				aServer.sendAll(new PlayerDefeatedMessage(p));
				aServer.sendAll(new ServerChatMessage("Player \"" + p.getNickname() + "\" has been defeated."));
			}
		}
		// Check if game is over
		final Player winner = aState.getWinner();
		if (winner != null) {
			announceWinner(winner);
		}
		// Reset stuff
		resetTurnMap();
		resetTimer();
	}

	@Override
	public void clientQuit(final com.jme3.network.Client client)
	{
		if (!aClientMap.containsKey(client)) {
			return; // the player doesn't exist, why is this being called?
		}
		final Player leaving = aClientMap.remove(client);
		aTurnMap.remove(leaving);
		if (aClientMap.size() == 1) {
			announceWinner(aClientMap.values().iterator().next());
		}
		if (aClientMap.isEmpty()) {
			// no more players, go ahead and stop server
			EverVoidServer.stop();
		}
	}

	/**
	 * @return The number of turns that have elapsed so far.
	 */
	public int getTurnNumber()
	{
		return aTurnNumber;
	}

	@Override
	public void messageReceived(final String type, final LobbyState lobby, final HostedConnection client, final Json content)
	{
		if (type.equals(TurnMessage.class.getName())) {
			addAllMoves(client, new Turn(content, aState));
			tryCalculateTurn();
		}
		else if (type.equals(StartGameMessage.class.getName())) {
			final List<Player> playerList = new ArrayList<Player>();
			for (final LobbyPlayer player : lobby) {
				final Player p = new Player(player.getNickname(), player.getRace(), player.getColorName(), aGameData);
				playerList.add(p);
				aClientMap.put(player.getClient(), p);
				aTurnMap.put(p, null);
			}
			if (playerList.size() == 1) {
				// there is only one player, add an "AI"
				String randomColor;
				boolean taken;
				do {
					taken = false;
					randomColor = aGameData.getRandomColor();
					for (final LobbyPlayer p : lobby) {
						if (p.getColorName().equals(randomColor)) {
							taken = true;
						}
					}
				}
				while (taken);
				playerList.add(new Player(aServer.getNewPlayerNickame(), aGameData.getRandomRace(), randomColor, aGameData));
			}
			final EVGameState tempState = new EVGameState(playerList, aGameData);
			aReadyMap = new HashSet<HostedConnection>(aClientMap.keySet());
			aServer.sendAllState(tempState);
			setState(tempState);
		}
		else if (type.equals(ReadyMessage.class.getName())) {
			// wait until all players are ready
			if (aReadyMap != null) {
				aReadyMap.remove(client);
			}
			if (aReadyMap.isEmpty()) {
				// actually start game
				resetTimer();
				aReadyMap = null;
			}
		}
		else if (type.equals(LoadGameRequest.class.getName())) {
			try {
				setState(new EVGameState(content));
			}
			catch (final BadJsonInitialization e) {
				// This should never happen, it is never called without checking
				e.printStackTrace();
			}
			for (final LobbyPlayer player : lobby) {
				final Player p = aState.getPlayerByNickname(player.getNickname());
				if (p == null) {
					// We're really in trouble here
					aGameEngineLog.severe("Cannot find player from loaded game corresponding to LobbyPlayer "
							+ player.getNickname());
				}
				aClientMap.put(player.getClient(), p);
				aTurnMap.put(p, null);
			}
			aReadyMap = new HashSet<HostedConnection>(aClientMap.keySet());
			aServer.sendAllState(aState);
			resetTimer();
		}
		else if (type.equals(RequestGameState.class.getName())) {
			aGameEngineLog.info("Got game state request from client " + client);
			final String clientHash = content.getStringAttribute("gamehash");
			final String thisHash = aState.toJson().getHash();
			aGameEngineLog.info("Client hash is " + clientHash + "; server hash is " + thisHash);
			aServer.send(client, new SaveGameStateReply(clientHash.equals(thisHash) ? null : aState));
		}
	}

	/**
	 * Finds all Planets in need of regeneration and regenerates them by the correct amounts.
	 * 
	 * @return The actions that will regenerate all Planets appropriate.
	 */
	private List<Action> regeneratePlanets()
	{
		final List<Action> actions = new ArrayList<Action>();
		for (final Planet p : aState.getAllPlanets()) {
			if (!p.getPlayer().isNullPlayer() && (!p.isAtMaxHealth() || !p.isAtMaxShields())) {
				// needs to regenerate
				try {
					actions.add(new RegeneratePlanet(p));
				}
				catch (final IllegalEVActionException e) {
					// should never happen
				}
			}
		}
		return actions;
	}

	/**
	 * Finds all Ships in need of regeneration and regenerates them by the correct amounts.
	 * 
	 * @return The actions that will regenerate all Ship appropriate.
	 */
	private List<Action> regenerateShips()
	{
		final List<Action> actions = new ArrayList<Action>();
		for (final Ship s : aState.getAllShips()) {
			if (!s.isAtMaxHealth() || !s.isAtMaxRadiation() || !s.isAtMaxShields()) {
				try {
					actions.add(new RegenerateShip(s));
				}
				catch (final IllegalEVActionException e) {
					// Should never happen
				}
			}
		}
		return actions;
	}

	/**
	 * Resets the timer that determines when turns are calculated, a small buffer of time is given for messages to arrive.
	 */
	public void resetTimer()
	{
		// Cancel current timer and remove the scheduled task from the list
		aTurnTimer.cancel();
		aTurnTimer = new Timer();
		aTurnTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				calculateTurn();
			}
		}, 1000 * aGameData.getTurnLength() + Math.max(3000, (int) (3 * aServer.maxPingTime())));
		// give some time for late turns to come in
	}

	/**
	 * Wipes all moves currently received from players and ready to be compiled. This should be done from calculateTurn() once
	 * all moves have been dealt with.
	 */
	private void resetTurnMap()
	{
		for (final Player p : aTurnMap.keySet()) {
			aTurnMap.put(p, null);
		}
	}

	/**
	 * @param state
	 *            The state to set.
	 */
	protected void setState(final EVGameState state)
	{
		aState = state;
	}

	/**
	 * Extract the action of the specified types from the specified turn, and reenqueues them in random order at the end of the
	 * turn
	 * 
	 * @param turn
	 *            The turn to extract from (and to reenqueue to)
	 * @param types
	 *            The types of action to extract
	 */
	private void shakeUpActions(final Turn turn, final Class<?>... types)
	{
		final List<Action> actionsCopy = new ArrayList<Action>(turn.getActionsOfType(types));
		Collections.shuffle(actionsCopy); // Shake it up
		for (final Action act : actionsCopy) {
			turn.reEnqueueAction(act);
		}
	}

	@Override
	public void stop()
	{
		aTurnTimer.cancel();
		aTurnTimer.purge();
		aClientMap.clear();
		aTurnMap.clear();
	}

	/**
	 * Wait on all players to send moves in before calculating a turn.
	 * 
	 * @return Whether a turn was calculated.
	 */
	private boolean tryCalculateTurn()
	{
		for (final Player p : aTurnMap.keySet()) {
			if (aTurnMap.get(p) == null) {
				return false;
			}
		}
		calculateTurn();
		return true;
	}
}

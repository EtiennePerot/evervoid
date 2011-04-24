package com.evervoid.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.action.Action;
import com.evervoid.state.action.Turn;
import com.evervoid.state.building.Building;
import com.evervoid.state.data.BadJsonInitialization;
import com.evervoid.state.data.BuildingData;
import com.evervoid.state.data.GameData;
import com.evervoid.state.data.PlanetData;
import com.evervoid.state.data.RaceData;
import com.evervoid.state.data.ResourceData;
import com.evervoid.state.data.StarData;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Prop;
import com.evervoid.state.prop.Ship;
import com.evervoid.state.prop.Star;
import com.evervoid.utils.EVContainer;
import com.evervoid.utils.LoggerUtils;
import com.evervoid.utils.MathUtils;

public class EVGameState implements Jsonable
{
	public static final String sNeutralPlayerName = "NullPlayer";
	private static Set<String> sRandomSolarSystemNames = null;

	/**
	 * Reads a list of solar system names from res/schema/ssnames.json . Chooses a random element from the list and returns it.
	 * Duplicate names are kept track of until sRandomSolarSystemNames is reset to null.
	 * 
	 * @return A random unused name for a SolarSystem.
	 */
	public static String getRandomSolarSystemName()
	{
		if (sRandomSolarSystemNames == null) {
			// get the Json from the file
			final Json j = Json.fromFile("schema/ssnames.json");
			// load every name into memory
			sRandomSolarSystemNames = new HashSet<String>();
			for (final Json name : j.getListAttribute("names")) {
				sRandomSolarSystemNames.add(name.getString());
			}
		}
		final String rand = MathUtils.getRandomElement(sRandomSolarSystemNames);
		sRandomSolarSystemNames.remove(rand);
		return rand;
	}

	/**
	 * A map of id to Building.
	 */
	private final Map<Integer, Building> aAllBuildings = new HashMap<Integer, Building>();
	/**
	 * A map from id to Prop.
	 */
	private final Map<Integer, Prop> aAllProps = new HashMap<Integer, Prop>();
	/**
	 * The galaxy associated with this state.
	 */
	protected Galaxy aGalaxy;
	/**
	 * The GamData associated with this state.
	 */
	private final GameData aGameData;
	/**
	 * Whether the game has started.
	 */
	private boolean aGameStarted = false;
	/**
	 * The null player. All unclaimed planets and SolarSystems belong to the null player.
	 */
	private final Player aNullPlayer;
	/**
	 * The list of all Players, excluding the NullPlayer.
	 */
	private final List<Player> aPlayerList;

	/**
	 * Creates a GameState from the contents of the Json.
	 * 
	 * @param json
	 *            The Json representation of the game state
	 * @throws BadJsonInitialization
	 */
	public EVGameState(final Json json) throws BadJsonInitialization
	{
		// read Json attributes
		aGameStarted = json.getBooleanAttribute("gamestarted");
		aGameData = new GameData(json.getAttribute("gamedata"));
		// read players
		final Json players = json.getAttribute("players");
		aPlayerList = new ArrayList<Player>(players.size());
		for (final Json p : players) {
			aPlayerList.add(new Player(p, this));
		}
		// create null player if it doesn't exist yet
		if (getPlayerByName(sNeutralPlayerName) != null) {
			aNullPlayer = getPlayerByName(sNeutralPlayerName);
		}
		else {
			aNullPlayer = new Player(sNeutralPlayerName, "red", "round", this);
			aPlayerList.add(aNullPlayer);
		}
		// It is necessary to create an empty galaxy first and then to populate it.
		// This is because certain stuff (props) need to look up stuff from the game state when constructing themselves
		// Thus aGalaxy must be defined in the game state before they can look that up
		aGalaxy = new Galaxy(json.getAttribute("galaxy"), this);
	}

	/**
	 * Creates a fully randomized galaxy with solar systems and planets in it.
	 */
	public EVGameState(final List<Player> playerList, final GameData data)
	{
		aGameData = data;
		aPlayerList = playerList;
		aNullPlayer = new Player(sNeutralPlayerName, "round", "red", this);
		aPlayerList.add(aNullPlayer);
		aGalaxy = new Galaxy(this);
		aGalaxy.populateGalaxy();
		// Temporary set to keep track of which solar systems haven't been assigned yet
		final Set<SolarSystem> solarSystems = new HashSet<SolarSystem>(aGalaxy.getSolarSystems());
		for (final Player p : aPlayerList) {
			final SolarSystem home = MathUtils.getRandomElement(solarSystems);
			solarSystems.remove(home);
			p.setState(this);
			p.setHomeSolarSystem(home);
		}
		aGalaxy.populateSolarSystems();
		// we should be done with random naming, let's get rid of this big list.
		sRandomSolarSystemNames = null;
	}

	@Override
	public EVGameState clone()
	{
		try {
			return new EVGameState(toJson());
		}
		catch (final BadJsonInitialization e) {
			// this should never happen
			// if it does, this mean the toJson() is not in sync with the constructor
			// this is bad news all around
			LoggerUtils
					.severe("Error caught in State cloning. This is bad news, it very likely means the toJson() is having trouble");
			return null;
		}
	}

	/**
	 * Commits a single Action on this state.
	 * 
	 * @param action
	 *            The Action to commit
	 * @return Whether or no the Action executed successfully.
	 */
	public boolean commitAction(final Action action)
	{
		return action.execute();
	}

	/**
	 * Commits a turn on the state. Actions are check for validity before they are actually executed on this state. The function
	 * returns a Turn comprised of the Actions that were successfully executed on this state. These actions will trigger all the
	 * correct observer calls from the state objects.
	 * 
	 * @param turn
	 *            The Turn to commit.
	 * @return The Turn that successfully committed.
	 */
	public Turn commitTurn(final Turn turn)
	{
		final Turn newTurn = new Turn();
		for (final Action action : turn.getActions()) {
			if (commitAction(action)) {
				newTurn.addAction(action);
			}
		}
		return newTurn;
	}

	/**
	 * Removes a building from the building set.
	 * 
	 * @param buildingID
	 *            The id of the Building to remove.
	 */
	public void deregisterBuilding(final int buildingID)
	{
		aAllBuildings.remove(buildingID);
	}

	public void deregisterProp(final int propID)
	{
		aAllProps.remove(propID);
	}

	/**
	 * @return A set of all Planets registered to this state.
	 */
	public Set<Planet> getAllPlanets()
	{
		final Set<Planet> planets = new HashSet<Planet>();
		for (final Prop p : aAllProps.values()) {
			if (p instanceof Planet) {
				planets.add((Planet) p);
			}
		}
		return planets;
	}

	/**
	 * @return A set of all ships registered to this state.
	 */
	public Set<Ship> getAllShips()
	{
		final Set<Ship> ships = new HashSet<Ship>();
		for (final Prop p : aAllProps.values()) {
			if (p instanceof Ship) {
				ships.add((Ship) p);
			}
		}
		return ships;
	}

	/**
	 * @return The building data associated with this race and building name.
	 */
	public BuildingData getBuildingData(final String race, final String building)
	{
		return aGameData.getBuildingData(race, building);
	}

	/**
	 * @return The Building associated with this building id.
	 */
	public Building getBuildingFromID(final int id)
	{
		return aAllBuildings.get(id);
	}

	/**
	 * @return This state's Data.
	 */
	public GameData getData()
	{
		return aGameData;
	}

	/**
	 * @return This state's Galaxy.
	 */
	public Galaxy getGalaxy()
	{
		return aGalaxy;
	}

	/**
	 * @return The global jump cost as defined by the GameData.
	 */
	public int getJumpCost()
	{
		return aGameData.getJumpCost();
	}

	/**
	 * @return The next unused prop id.
	 */
	public int getNextBuildingID()
	{
		if (aAllBuildings.isEmpty()) {
			return 0;
		}
		int maxID = Integer.MIN_VALUE;
		for (final int id : aAllBuildings.keySet()) {
			maxID = Math.max(maxID, id);
		}
		return maxID + 1;
	}

	/**
	 * @return A new, unused prop id.
	 */
	public int getNextPropID()
	{
		// If we have no prop, then ID 0 is not taken
		if (aAllProps.isEmpty()) {
			return 0;
		}
		// If we have props, iterate over them, get the max, and return max+1
		// because that ID is certainly not taken
		int maxId = Integer.MIN_VALUE;
		for (final Integer id : aAllProps.keySet()) {
			maxId = Math.max(maxId, id);
		}
		return maxId + 1;
	}

	/**
	 * @return A new, unused solar system id.
	 */
	public int getNextSolarID()
	{
		return aGalaxy.getNextSolarID();
	}

	/**
	 * @return The Wormhole associated with the id.
	 */
	public int getNextWormholeID()
	{
		return aGalaxy.getNextWormholeID();
	}

	/**
	 * @return The Null (neutral) player.
	 */
	public Player getNullPlayer()
	{
		return aNullPlayer;
	}

	/**
	 * @return The number of players in the game, not counting the NullPlayer.
	 */
	public int getNumOfPlayers()
	{
		// -1 for NullPlayer
		return aPlayerList.size() - 1;
	}

	/**
	 * @return All the planets owned by a particular player.
	 */
	public Set<Planet> getPlanetByPlayer(final Player player)
	{
		final Set<Planet> planetSet = new HashSet<Planet>();
		for (final Prop p : aAllProps.values()) {
			if (p instanceof Planet && p.getPlayer().equals(player)) {
				planetSet.add((Planet) p);
			}
		}
		return planetSet;
	}

	/**
	 * @param planetType
	 *            The type of planet
	 * @return The PlanetData object corresponding to that planet type
	 */
	public PlanetData getPlanetData(final String planetType)
	{
		return aGameData.getPlanetData(planetType);
	}

	/**
	 * @return Available planet types
	 */
	public Set<String> getPlanetTypes()
	{
		return aGameData.getPlanetTypes();
	}

	/**
	 * Returns a Player by his/her name
	 * 
	 * @param name
	 *            The name to search for
	 * @return The player object
	 */
	public Player getPlayerByName(final String name)
	{
		for (final Player p : aPlayerList) {
			if (p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Returns a Player by his/her nickname
	 * 
	 * @param nickname
	 *            The nickname to search for
	 * @return The player object
	 */
	public Player getPlayerByNickname(final String name)
	{
		for (final Player p : aPlayerList) {
			if (p.getNickname().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * @param colorName
	 *            The name of the player's color
	 * @return The color of that name
	 */
	public Color getPlayerColor(final String colorName)
	{
		return aGameData.getPlayerColor(colorName);
	}

	/**
	 * @return The list of players
	 */
	public List<Player> getPlayers()
	{
		return aPlayerList;
	}

	/**
	 * Look up a Prop in the state.
	 * 
	 * @param id
	 *            The prop ID.
	 * @return The Prop object, or null if there is no such prop.
	 */
	public Prop getPropFromID(final int id)
	{
		return aAllProps.get(id);
	}

	/**
	 * @param raceType
	 *            The type of race
	 * @return The RaceData object corresponding to that race type
	 */
	public RaceData getRaceData(final String raceType)
	{
		return aGameData.getRaceData(raceType);
	}

	/**
	 * @return A randomly-selected player; this will never be the Null Player
	 */
	Player getRandomPlayer()
	{
		Player rand = MathUtils.getRandomElement(aPlayerList);
		while (rand.equals(aNullPlayer)) {
			rand = MathUtils.getRandomElement(aPlayerList);
		}
		return rand;
	}

	/**
	 * @return A star with a radomly chosen sprite, but dimension set as the parameter.
	 */
	public Star getRandomStar(final Dimension dim)
	{
		return Star.randomStar(dim, this);
	}

	/**
	 * @return The ResourceData associated with the resourceName
	 */
	public ResourceData getResourceData(final String resourceName)
	{
		return aGameData.getResourceData(resourceName);
	}

	/**
	 * @return The names of all the resources in GameData.
	 */
	public Set<String> getResourceNames()
	{
		return aGameData.getResources();
	}

	/**
	 * @param id
	 *            The ID of the solar system
	 * @return The solar system of the specified ID.
	 */
	public SolarSystem getSolarSystem(final int id)
	{
		return aGalaxy.getSolarSystem(id);
	}

	/**
	 * @return The list of solar systems in the galaxy
	 */
	public Collection<SolarSystem> getSolarSystems()
	{
		return aGalaxy.getSolarSystems();
	}

	/**
	 * @param starType
	 *            The type of star
	 * @return The StarData object corresponding to that star type
	 */
	public StarData getStarData(final String starType)
	{
		return aGameData.getStarData(starType);
	}

	/**
	 * @return Available star types
	 */
	public Set<String> getStarTypes()
	{
		return aGameData.getStarTypes();
	}

	/**
	 * @return The winner if there is one, or null if the game is not over yet
	 */
	public Player getWinner()
	{
		final List<Player> playersLeft = new ArrayList<Player>(aPlayerList);
		for (final Player p : aPlayerList) {
			if (p.isNullPlayer() || hasLost(p)) {
				playersLeft.remove(p);
			}
		}
		if (playersLeft.size() == 1) {
			return playersLeft.get(0);
		}
		return null;
	}

	/**
	 * @return The Wormhole associated with the id.
	 */
	public Wormhole getWormhole(final int id)
	{
		return aGalaxy.getWormhole(id);
	}

	/**
	 * Checks whether a player has lost or not
	 * 
	 * @param player
	 *            The player to check
	 * @return True if the player has lost
	 */
	public boolean hasLost(final Player player)
	{
		// Defeat condition 1: Planet ownership (A player loses if he loses all his planets)
		for (final Prop prop : aAllProps.values()) {
			if (prop instanceof Planet && prop.getPlayer().equals(player)) {
				return false;
			}
		}
		// TODO: Insert other defeat conditions here
		return true;
	}

	/**
	 * @return Whether the game has started or not (in lobby)
	 */
	public boolean isStarted()
	{
		return aGameStarted;
	}

	/**
	 * @return Whether the game is ready to be started (all players ready, all slots filled)
	 */
	public boolean readyToStart()
	{
		// FIXME: Check if all players are ready
		return true;
	}

	/**
	 * Registers a building to the state.
	 * 
	 * @param building
	 *            The building to register.
	 */
	public void registerBuilding(final Building building)
	{
		aAllBuildings.put(building.getID(), building);
	}

	/**
	 * Adds a prop to the game state's list of props
	 * 
	 * @param prop
	 *            The prop to add
	 */
	public void registerProp(final Prop prop)
	{
		aAllProps.put(prop.getID(), prop);
	}

	/**
	 * Registers a prop to the state, then adds it to the specified container.
	 * 
	 * @param prop
	 *            The prop to be registered.
	 * @param container
	 *            The container into which the prop is being placed.
	 */
	public void registerProp(final Prop prop, final EVContainer<Prop> container)
	{
		registerProp(prop);
		prop.enterContainer(container);
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setBooleanAttribute("gamestarted", aGameStarted);
		j.setAttribute("gamedata", aGameData);
		j.setAttribute("galaxy", aGalaxy);
		j.setListAttribute("players", aPlayerList);
		return j;
	}
}

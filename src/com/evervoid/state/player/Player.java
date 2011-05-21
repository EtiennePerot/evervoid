package com.evervoid.state.player;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Color;
import com.evervoid.state.EVGameState;
import com.evervoid.state.SolarSystem;
import com.evervoid.state.data.GameData;
import com.evervoid.state.data.RaceData;
import com.evervoid.state.observers.PlayerObserver;
import com.evervoid.state.prop.Planet;

public class Player implements Jsonable
{
	/**
	 * The color of the Player's overlay.
	 */
	private Color aColor;
	private final GameData aData;
	/**
	 * Player's home solar system ID; assigned by the game state on creation.
	 */
	private int aHomeSolarSystem = -1;
	/**
	 * Internal player name, used to store associations.
	 */
	private final String aName;
	/**
	 * UI-visible player name.
	 */
	private String aNickname = "";
	/**
	 * The set of all object observing this Player.
	 */
	private final Set<PlayerObserver> aObserverList = new HashSet<PlayerObserver>();
	/**
	 * The Player's RaceData.
	 */
	private RaceData aRaceData;
	/**
	 * The Payer's current Research;
	 */
	private Research aResearch;
	/**
	 * The Player's current available Resources.
	 */
	private ResourceAmount aResources;
	/**
	 * The State this Player belongs to.
	 */
	private EVGameState aState;

	/**
	 * Creates a Player from the contents of the Json.
	 * 
	 * @param j
	 *            The Json from which to build the Player.
	 * @param state
	 *            The State this Player belongs to.
	 */
	public Player(final Json j, final EVGameState state)
	{
		this(j.getStringAttribute("name"), j.getStringAttribute("race"), j.getStringAttribute("color"), state);
		aNickname = j.getStringAttribute("friendlyname");
		aResearch = Research.fromJson(j.getAttribute("research"));
		aHomeSolarSystem = j.getIntAttribute("home");
		aResources = new ResourceAmount(j.getAttribute("resources"));
	}

	/**
	 * Player constructor. Warning: The state argument may be null during lobby initialization; do NOT rely on the state in this
	 * method. Rely on it in the setState method instead.
	 * 
	 * @param name
	 *            Player name
	 * @param race
	 *            Player race name
	 * @param color
	 *            Player color name
	 * @param state
	 *            Pointer to state; MAY BE null, see setState()
	 */
	public Player(final String name, final String race, final String color, final EVGameState state)
	{
		aName = name;
		aData = state.getData();
		aRaceData = aData.getRaceData(race);
		aColor = aData.getPlayerColor(color);
		aResearch = new Research();
		aNickname = aName; // Can be set by the player later
		setState(state); // Will populate the rest
	}

	/**
	 * Creates a Player with the passed parameters. The GameState will be null, and so much be set later with setState(). The
	 * Color and RaceData objects are taken from the GameData object.
	 * 
	 * @param name
	 *            The Player's nickname in game.
	 * @param race
	 *            The String representation of the Player's Race.
	 * @param color
	 *            The String representation of the Player's color.
	 * @param data
	 *            The String representation of the Player's data.
	 */
	public Player(final String name, final String race, final String color, final GameData data)
	{
		aName = name;
		aData = data;
		aRaceData = aData.getRaceData(race);
		aColor = aData.getPlayerColor(color);
		aResearch = new Research();
		aNickname = aName; // Can be set by the player later
	}

	/**
	 * Adds the given resources to the Player's current total.
	 * 
	 * @param amount
	 *            The resources to add.
	 * @return The Player's new resources.
	 */
	public boolean addResources(final ResourceAmount amount)
	{
		final ResourceAmount newAmount = aResources.add(amount);
		if (newAmount != null) { // Successful add
			aResources = newAmount;
			for (final PlayerObserver observer : aObserverList) {
				observer.playerReceivedIncome(this, amount);
			}
		}
		return newAmount != null;
	}

	public void deregisterObserver(final PlayerObserver observer)
	{
		aObserverList.remove(observer);
	}

	/**
	 * @return The set of String representation of the Buildings which this Player can build.
	 */
	public Set<String> getBuildings()
	{
		return aRaceData.getBuildings();
	}

	/**
	 * @return The color associated with this Player's by the State.
	 */
	public Color getColor()
	{
		return aColor;
	}

	/**
	 * @return The Player's current income.
	 */
	public ResourceAmount getCurrentIncome()
	{
		ResourceAmount income = aResources.emptyClone();
		for (final Planet planet : aState.getPlanetByPlayer(this)) {
			income = income.add(planet.getResourceRate());
		}
		return income;
	}

	/**
	 * @return The SolarSystem designated as this Player's home SolarSystem.
	 */
	public SolarSystem getHomeSolarSystem()
	{
		return aState.getSolarSystem(aHomeSolarSystem);
	}

	/**
	 * @return This Player's name in the state.
	 */
	public String getName()
	{
		return aName;
	}

	/**
	 * @return The name this Player displays to other users.
	 */
	public String getNickname()
	{
		return aNickname;
	}

	/**
	 * @return This Player's RaceData.
	 */
	public RaceData getRaceData()
	{
		return aRaceData;
	}

	/**
	 * @return This Player's current research level.
	 */
	public Research getResearch()
	{
		return aResearch;
	}

	/**
	 * @return This Player's current resources.
	 */
	public ResourceAmount getResources()
	{
		return aResources.clone();
	}

	/**
	 * @return The State this Player belongs to.
	 */
	public EVGameState getState()
	{
		return aState;
	}

	/**
	 * @return Whether the Player currently has that number of resources availiable.
	 */
	public boolean hasResources(final ResourceAmount cost)
	{
		return aResources.contains(cost);
	}

	/**
	 * @return Whether this Player is the NULL Player.
	 */
	public boolean isNullPlayer()
	{
		return EVGameState.sNeutralPlayerName.equals(aName);
	}

	public void registerObserver(final PlayerObserver observer)
	{
		aObserverList.add(observer);
	}

	/**
	 * Changes the Player's color to be that of the parameter Color.
	 * 
	 * @return The modified Player.
	 */
	public Player setColor(final Color color)
	{
		aColor = color;
		return this;
	}

	public void setHomeSolarSystem(final SolarSystem home)
	{
		aHomeSolarSystem = home.getID();
	}

	/**
	 * Changes the Player's nickname
	 * 
	 * @param name
	 *            The new nickname.
	 */
	public void setNickname(final String name)
	{
		aNickname = name;
	}

	/**
	 * Changes the Player's race to be the parameter race.
	 * 
	 * @param race
	 *            The race to set.
	 * @return The modified Player.
	 */
	public Player setRace(final RaceData race)
	{
		aRaceData = race;
		return this;
	}

	/**
	 * Set this player's EVGameState link, and update Player information based on it
	 * 
	 * @param state
	 *            The EVGameState to set to
	 */
	public void setState(final EVGameState state)
	{
		aState = state;
		if (aState != null) {
			aRaceData = aState.getRaceData(aRaceData.getType());
			aColor = aState.getPlayerColor(aColor.getName());
			if (aResources == null) {
				// If aResources is not null at this point, then it has already been loaded from Json; don't overwrite it!
				aResources = new ResourceAmount(state.getData(), aRaceData);
			}
		}
	}

	/**
	 * Make sure everything in amount is positive, all values are negated before being added to the player's resource.
	 * 
	 * @param amount
	 *            The amount to subtract
	 * @return Whether the subtraction was successful or not
	 */
	public boolean subtractResources(final ResourceAmount amount)
	{
		if (!hasResources(amount)) {
			return false;
		}
		final ResourceAmount newAmount = aResources.subtract(amount);
		if (newAmount != null) { // Successful subtract
			aResources = newAmount;
			for (final PlayerObserver observer : aObserverList) {
				// Negate amount to signify we have removed the resources
				observer.playerReceivedIncome(this, amount.negate());
			}
		}
		return newAmount != null;
	}

	@Override
	public Json toJson()
	{
		return new Json().setAttribute("name", aName).setAttribute("race", aRaceData.getType())
				.setAttribute("color", aColor.getName()).setAttribute("research", aResearch)
				.setAttribute("friendlyname", aNickname).setAttribute("home", aHomeSolarSystem)
				.setAttribute("resources", aResources);
	}

	@Override
	public String toString()
	{
		return "Player " + aName + " (Nicknamed \"" + aNickname + "\") of race " + aRaceData.getType();
	}
}

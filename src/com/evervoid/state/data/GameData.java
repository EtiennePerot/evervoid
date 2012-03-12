package com.evervoid.state.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.evervoid.json.BadJsonInitialization;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Color;
import com.evervoid.state.EVGameState;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Star;
import com.evervoid.utils.LoggerUtils;
import com.evervoid.utils.MathUtils;

/**
 * GameData contains all information pertinent to creating a new {@link EVGameState}. It contains all the Data for
 * building
 * Race, Planet, Ship, Resource, etc. as well as some global information, such as the base cost for ships to jump.
 */
public class GameData implements Jsonable
{
    /**
     * This method tests loading the default game data
     * 
     * @param args
     *            the argument to the main.
     */
    public static void main(final String[] args)
    {
        System.out.println("Loading GameData...");
        GameData data = null;
        try {
            data = new GameData();
        } catch (final BadJsonInitialization e1) {
            e1.printStackTrace();
        }
        System.out.println("GameData loaded. Re-serializing:");
        final Json jData = data.toJson();
        System.out.println(jData.toPrettyString());
        System.out.println("Recreating GameData based on re-serialized data...");
        GameData data2 = null;
        try {
            data2 = new GameData(jData);
        } catch (final BadJsonInitialization e) {
            e.printStackTrace();
        }
        final Json jData2 = data2.toJson();
        System.out.println(jData2.toPrettyString());
        System.out.println("Comparing both GameData hashes: ");
        System.out.println("Initial GameData: " + jData.getHash());
        System.out.println("Re-read GameData: " + jData2.getHash());
        System.out.println("Match: " + jData2.equals(jData));
    }

    /**
     * The base radiation cost for ships to jump.
     */
    private final int aJumpRadiationCost;
    /**
     * The set of all {@link Planet} defined in this GameData, mapped to their types.
     */
    private final Map<String, PlanetData> aPlanetData = new HashMap<String, PlanetData>();
    /**
     * The set of all {@link Player} {@link Color} defined in this GameData, mapped to their names.
     */
    private final Map<String, Color> aPlayerColors = new HashMap<String, Color>();
    /**
     * The set of all {@link RaceData} defined in this GameData, mapped to their types.
     */
    private final Map<String, RaceData> aRaceData = new HashMap<String, RaceData>();
    /**
     * The set of all {@link Resource} defined in this GameData, mapped to their types.
     */
    private final Map<String, ResourceData> aResources = new HashMap<String, ResourceData>();
    /**
     * The set of all {@link Star} define din this GameData, mapped to their types.
     */
    private final Map<String, StarData> aStarData = new HashMap<String, StarData>();
    /**
     * The maximum duration of each turn in seconds.
     */
    private final int aTurnDurationInSeconds;

    /**
     * Loads default game data from schema/gamedata.json
     * 
     * @throws BadJsonInitialization
     *             If the Json is not formatted correctly.
     */
    public GameData() throws BadJsonInitialization
    {
        this("schema/gamedata.json");
    }

    /**
     * Creates a new game data object from Json
     * 
     * @param j
     *            Parsed Json containing the game data
     * @throws BadJsonInitialization
     *             If the Json is not formatted correctly.
     */
    public GameData(final Json j) throws BadJsonInitialization
    {
        try {
            final Json starJson = j.getAttribute("star");
            for (final String star : starJson.getAttributes()) {
                aStarData.put(star, new StarData(star, starJson.getAttribute(star)));
            }
            final Json planetJson = j.getAttribute("planet");
            for (final String planet : planetJson.getAttributes()) {
                aPlanetData.put(planet, new PlanetData(planet, planetJson.getAttribute(planet)));
            }
            final Json raceJson = j.getAttribute("race");
            for (final String race : raceJson.getAttributes()) {
                aRaceData.put(race, new RaceData(race, raceJson.getAttribute(race)));
            }
            final Json resourceJson = j.getAttribute("resources");
            for (final String resource : resourceJson.getAttributes()) {
                aResources.put(resource, new ResourceData(resource, resourceJson.getAttribute(resource)));
            }
            final Json colorJson = j.getAttribute("playercolors");
            for (final String color : colorJson.getAttributes()) {
                aPlayerColors.put(color, new Color(color, colorJson.getAttribute(color)));
            }
        } catch (final Exception e) {
            LoggerUtils.severe("Caught error in Game Data loading, syntax is incorrect", e);
            throw new BadJsonInitialization();
        }
        aTurnDurationInSeconds = j.getIntAttribute("turnLength");
        aJumpRadiationCost = j.getIntAttribute("jumpCost");
    }

    /**
     * Attempts to create a GameData from the contents of the Json file located at the passed url.
     * 
     * @param filename
     *            The file from which to load the GameData.
     * @throws BadJsonInitialization
     *             If the File is not a correct Json.
     */
    public GameData(final String filename) throws BadJsonInitialization
    {
        this(Json.fromFile(filename));
    }

    /**
     * @param race
     *            The title of the race which can build the building.
     * @param building
     *            The title of the building.
     * @return The BuildingData of the given building for the given race. Will return null if no such element exists.
     */
    public BuildingData getBuildingData(final String race, final String building)
    {
        final RaceData raceData = aRaceData.get(race);
        if (raceData != null) {
            return raceData.getBuildingData(building);
        }
        return null;
    }

    /**
     * @return The base amount of radiation necessary to perform a jump.
     */
    public int getJumpRadiationCost()
    {
        return aJumpRadiationCost;
    }

    /**
     * @param planetType
     *            The title of the planet type.
     * @return The PlanetData associated with the given planet type.
     */
    public PlanetData getPlanetData(final String planetType)
    {
        return aPlanetData.get(planetType);
    }

    /**
     * @return The set of all planet types defined in this GameData.
     */
    public Set<String> getPlanetTypes()
    {
        return aPlanetData.keySet();
    }

    /**
     * @param colorname
     *            The name of the color.
     * @return The Color associated with the color name passed, null if not defined.
     */
    public Color getPlayerColor(final String colorname)
    {
        return aPlayerColors.get(colorname);
    }

    /**
     * @return The name of each Color in the set of all Colors that Players may take on.
     */
    public Set<String> getPlayerColors()
    {
        return aPlayerColors.keySet();
    }

    /**
     * @param raceType
     *            The title of the race type.
     * @return The RaceData for the given race type, null if not defined.
     */
    public RaceData getRaceData(final String raceType)
    {
        return aRaceData.get(raceType);
    }

    /**
     * @return The name of each Race in the set of Races defined by this GameData.
     */
    public Set<String> getRaceTypes()
    {
        return aRaceData.keySet();
    }

    /**
     * @return The name of a random Color from the set of Colors a Player may take on.
     */
    public String getRandomColor()
    {
        return MathUtils.getRandomElement(aPlayerColors.keySet());
    }

    /**
     * @return The name of a random Race from the set of all Races defined in this GameData.
     */
    public String getRandomRace()
    {
        return MathUtils.getRandomElement(aRaceData.keySet());
    }

    /**
     * @param resourceType
     *            The title of the resource type.
     * @return The ResourceType associated with the given resource type, null if not defined.
     */
    public ResourceData getResourceData(final String resourceType)
    {
        return aResources.get(resourceType);
    }

    /**
     * @return The name of all Resources defined in this GameData.
     */
    public Set<String> getResources()
    {
        return aResources.keySet();
    }

    /**
     * @param starType
     *            The title of the star type.
     * @return The StarData associated with the given star type, null if the element does not exist.
     */
    public StarData getStarData(final String starType)
    {
        return aStarData.get(starType);
    }

    /**
     * @return The names of all the Stars defined in this GameData.
     */
    public Set<String> getStarTypes()
    {
        return aStarData.keySet();
    }

    /**
     * @return The amount of time (in seconds) Players have to submit their Turns.
     */
    public int getTurnLength()
    {
        return aTurnDurationInSeconds;
    }

    @Override
    public Json toJson()
    {
        final Json j = new Json();
        j.setMapAttribute("star", aStarData);
        j.setMapAttribute("planet", aPlanetData);
        j.setMapAttribute("race", aRaceData);
        j.setMapAttribute("playercolors", aPlayerColors);
        j.setMapAttribute("resources", aResources);
        j.setAttribute("turnLength", aTurnDurationInSeconds);
        j.setAttribute("jumpCost", aJumpRadiationCost);
        return j;
    }
}

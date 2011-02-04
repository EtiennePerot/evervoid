package com.evervoid.state.player;

import com.evervoid.gamedata.RaceData;
import com.evervoid.gamedata.RaceData.Race;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;

public class Player implements Jsonable
{
	private static Player sNullPlayer = null;

	public static Player getNullPlayer()
	{
		if (sNullPlayer == null) {
			sNullPlayer = new Player("Neutral").setColor(new PlayerColor(0, 0, 0, 0)).setRace(RaceData.getRaceData("SQUARE"));
		}
		return sNullPlayer;
	}

	private PlayerColor aColor;
	private final String aName;
	private RaceData aRaceData;
	private final Research aResearch = new Research();

	public Player(final String name)
	{
		aName = name;
		aColor = PlayerColor.random(); // FIXME: Let the player choose his color
		aRaceData = RaceData.getRaceData("SQUARE");
	}

	public PlayerColor getColor()
	{
		return aColor;
	}

	public String getName()
	{
		return aName;
	}

	public Race getRace()
	{
		return aRaceData.getRace();
	}

	public RaceData getRaceData()
	{
		return aRaceData;
	}

	public Research getResearch()
	{
		return aResearch;
	}

	public Player setColor(final PlayerColor color)
	{
		aColor = color;
		return this;
	}

	public Player setRace(final RaceData race)
	{
		aRaceData = race;
		return this;
	}

	@Override
	public Json toJson()
	{
		return new Json().setStringAttribute("name", aName).setStringAttribute("race", aRaceData.getType())
				.setAttribute("color", aColor).setAttribute("research", aResearch);
	}
}

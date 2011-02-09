package com.evervoid.state.player;

import com.evervoid.gamedata.RaceData;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EverVoidGameState;

public class Player implements Jsonable
{
	private PlayerColor aColor;
	private final String aName;
	private RaceData aRaceData;
	private final Research aResearch = new Research();

	public Player(final String name, final EverVoidGameState state)
	{
		aName = name;
		aColor = PlayerColor.random(); // FIXME: Let the player choose his color
		// aRaceData = state.getRaceData("neutral");
		// FIXME: Don't set it to square by default; should be neutral
		aRaceData = state.getRaceData("square");
	}

	public PlayerColor getColor()
	{
		return aColor;
	}

	public String getName()
	{
		return aName;
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

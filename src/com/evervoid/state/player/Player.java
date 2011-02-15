package com.evervoid.state.player;

import com.evervoid.gamedata.RaceData;
import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.Color;
import com.evervoid.state.EverVoidGameState;

public class Player implements Jsonable
{
	private Color aColor;
	/**
	 * UI-visible player name
	 */
	private String aFriendlyName = "";
	/**
	 * Internal player name, used to store associations
	 */
	private final String aName;
	private RaceData aRaceData;
	private Research aResearch = new Research();

	public Player(final Json j, final EverVoidGameState state)
	{
		this(j.getStringAttribute("name"), state);
		aColor = Color.fromJson(j.getAttribute("color"));
		aFriendlyName = j.getStringAttribute("friendlyname");
		aResearch = Research.fromJson(j.getAttribute("research"));
	}

	public Player(final String name, final EverVoidGameState state)
	{
		aName = name;
		aColor = Color.random(); // FIXME: Let the player choose his color
		// aRaceData = state.getRaceData("neutral");
		// FIXME: Don't set it to square by default; should be neutral
		aRaceData = state.getRaceData("square");
	}

	public Color getColor()
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

	public Player setColor(final Color color)
	{
		aColor = color;
		return this;
	}

	public void setFriendlyName(final String name)
	{
		aFriendlyName = name;
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
				.setAttribute("color", aColor).setAttribute("research", aResearch)
				.setStringAttribute("friendlyname", aFriendlyName);
	}
}

package com.evervoid.state.prop;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.gamedata.RaceData.Race;
import com.evervoid.state.player.Research;

public class TrailInfo
{
	public static TrailInfo getRaceTrail(final Race race, final Research research)
	{
		return new TrailInfo(race);
	}

	public String baseSprite;
	public float decayTime;
	public float distanceInterval;
	public String engineSprite;
	public Race race;
	public List<String> trailSprites = new ArrayList<String>();

	private TrailInfo(final Race race)
	{
		this.race = race;
		switch (race) {
			// TODO: Make these depend on research
			case ROUND:
				decayTime = 0.6f;
				distanceInterval = 24;
				baseSprite = "ships/round/trail.png";
				engineSprite = "ships/round/engine_1.png";
				break;
			case SQUARE:
				for (int i = 1; i <= 4; i++) {
					trailSprites.add("ships/square/trail." + i + ".png");
				}
				engineSprite = "ships/square/engine_1.png";
				break;
		}
	}
}

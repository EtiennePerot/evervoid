package com.evervoid.state.prop;

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
	public Race race;

	private TrailInfo(final Race race)
	{
		this.race = race;
		switch (race) {
			case ROUND:
				// TODO: Make these depend on research
				decayTime = 0.6f;
				distanceInterval = 24;
				baseSprite = "ships/round/trail.png";
			case SQUARE:
				// Nothing
		}
	}
}

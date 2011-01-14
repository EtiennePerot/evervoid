package com.evervoid.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.evervoid.engine.player.Player;
import com.evervoid.engine.solar.SolarSystem;

public class EverVoidGameState
{
	List<Player> aPlayerList;
	Set<SolarSystem> aSolarList;

	private EverVoidGameState()
	{
		aPlayerList = new ArrayList<Player>();
		aPlayerList.add(new Player("EverVoidGame"));
	}
}

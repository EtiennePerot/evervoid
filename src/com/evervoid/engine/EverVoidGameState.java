package com.evervoid.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.evervoid.engine.map.Galaxy;
import com.evervoid.engine.player.Player;

public class EverVoidGameState
{
	Set<Galaxy> aGalaxyList;
	List<Player> aPlayerList;

	private EverVoidGameState()
	{
		aPlayerList = new ArrayList<Player>();
		aPlayerList.add(new Player("EverVoidGame"));
	}
}

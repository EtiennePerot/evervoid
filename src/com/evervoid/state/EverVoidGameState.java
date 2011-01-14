package com.evervoid.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.evervoid.state.player.Player;
import com.evervoid.state.solar.SolarSystem;

public class EverVoidGameState
{
	List<Player> aPlayerList;
	Set<SolarSystem> aSolarList;

	private EverVoidGameState()
	{
		aPlayerList = new ArrayList<Player>();
		aPlayerList.add(new Player("EverVoidGame"));
		aSolarList.add(new SolarSystem(48));
	}
}

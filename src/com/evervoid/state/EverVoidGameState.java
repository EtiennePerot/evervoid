package com.evervoid.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.evervoid.client.ClientView;
import com.evervoid.state.player.Player;

public class EverVoidGameState
{
	List<Player> aPlayerList;
	Set<SolarSystem> aSolarList;

	public EverVoidGameState()
	{
		aPlayerList = new ArrayList<Player>();
		aPlayerList.add(new Player("EverVoidGame"));
		aSolarList.add(new SolarSystem(48));
	}

	@Override
	public EverVoidGameState clone()
	{
		// TODO actually clone
		return this;
	}

	public ClientView getSolarSystem(final Point3D point3d)
	{
		// TODO make return correct solar system
		return null;
	}
}

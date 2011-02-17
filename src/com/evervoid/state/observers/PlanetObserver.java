package com.evervoid.state.observers;

import com.evervoid.state.Building;
import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Ship;

public interface PlanetObserver
{
	public void buildingConstructed(Building building, int progress);

	public void caputred(Player player);

	public void shipConstructed(Ship ship, int progress);
}

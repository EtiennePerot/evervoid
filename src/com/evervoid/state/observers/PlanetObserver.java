package com.evervoid.state.observers;

import com.evervoid.state.player.Player;
import com.evervoid.state.prop.Planet;

public interface PlanetObserver
{
	public void buildingsChanged(Planet planet);

	public void planetCaptured(Planet planet, Player player);
}

package com.evervoid.client;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.views.galaxy.GalaxyView;
import com.evervoid.client.views.solar.SolarSystemView;
import com.evervoid.state.EverVoidGameState;
import com.evervoid.state.SolarSystem;

public class ViewManager
{
	public enum ViewTypes
	{
		GalaxyView, MenuView, PlanetView, SolarView
	}

	/**
	 * the galaxy view, stored as player will often be returning to this
	 */
	private GalaxyView aGalaxyView;
	private final Map<SolarSystem, SolarSystemView> aSolarViewSet = new HashMap<SolarSystem, SolarSystemView>();
	/**
	 * Instance of current game view, such as solar system view, galaxy view, etc.
	 */
	protected ClientView currentGameView;

	protected void createViews(final EverVoidGameState pState)
	{
		aGalaxyView = new GalaxyView(pState.getGalaxy());
		// set the default view
		currentGameView = getSolarView(null);
	}

	private ClientView getSolarView(final SolarSystem ss)
	{
		SolarSystem ssToUse = ss;
		if (ssToUse == null) {
			ssToUse = EverVoidClient.sGameState.getTempSolarSystem();
		}
		if (aSolarViewSet.containsKey(ssToUse)) {
			return aSolarViewSet.get(ssToUse);
		}
		final SolarSystemView ssView = new SolarSystemView(ssToUse);
		aSolarViewSet.put(ssToUse, ssView);
		return ssView;
	}

	public ClientView getView(final ViewTypes type, final Object arg)
	{
		currentGameView.removeFromParent();
		ClientView tempView = null;
		switch (type) {
			case GalaxyView:
				tempView = aGalaxyView;
				break;
			case SolarView:
				tempView = getSolarView((SolarSystem) arg);
				break;
		}
		return tempView;
	}
}

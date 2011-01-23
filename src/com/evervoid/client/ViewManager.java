package com.evervoid.client;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.views.galaxy.GalaxyView;
import com.evervoid.client.views.solar.SolarSystemView;
import com.evervoid.state.EverVoidGameState;
import com.evervoid.state.Point3D;

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
	private final Map<Point3D, SolarSystemView> aSolarViewSet = new HashMap<Point3D, SolarSystemView>();
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

	private ClientView getSolarView(final Point3D point)
	{
		if (aSolarViewSet.containsKey(point)) {
			return aSolarViewSet.get(point);
		}
		final SolarSystemView ssView = new SolarSystemView(EverVoidClient.sGameState.getSolarSystem(point));
		aSolarViewSet.put(point, ssView);
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
				tempView = getSolarView((Point3D) arg);
				break;
		}
		return tempView;
	}
}

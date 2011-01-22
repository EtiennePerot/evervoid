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

	public ClientView getView(final ViewTypes type, final Object arg)
	{
		currentGameView.removeFromParent();
		ClientView tempView = null;
		switch (type) {
			case GalaxyView:
				tempView = aGalaxyView;
				break;
			case SolarView:
				final Point3D point = (Point3D) arg;
				tempView = aSolarViewSet.get(point);
				if (tempView == null) {
					tempView = new SolarSystemView(EverVoidClient.sGameState.getSolarSystem(point));
					aSolarViewSet.put(point, (SolarSystemView) tempView);
				}
				break;
		}
		return tempView;
	}

	protected void setViews(final EverVoidGameState pState)
	{
		aGalaxyView = new GalaxyView(pState.getGalaxy());
		currentGameView = new SolarSystemView(pState.getSolarSystem(null));
	}
}

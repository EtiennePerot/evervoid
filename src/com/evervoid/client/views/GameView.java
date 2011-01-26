package com.evervoid.client.views;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.ClientView;
import com.evervoid.client.views.galaxy.GalaxyView;
import com.evervoid.client.views.solar.SolarSystemView;
import com.evervoid.state.EverVoidGameState;
import com.evervoid.state.SolarSystem;
import com.jme3.math.Vector2f;

public class GameView extends ClientView
{
	public enum GameViewType
	{
		GALAXY, PLANET, SOLAR;
	}

	private static GameView sInstance = null;

	public static void changeView(final GameViewType type)
	{
		changeView(type, null);
	}

	public static void changeView(final GameViewType type, final Object arg)
	{
		sInstance.switchView(type, arg);
	}

	private ClientView aActiveView = null;
	/**
	 * the galaxy view, stored as player will often be returning to this
	 */
	private final GalaxyView aGalaxyView;
	private final Map<SolarSystem, SolarSystemView> aSolarViews = new HashMap<SolarSystem, SolarSystemView>();
	private final EverVoidGameState aState;

	public GameView(final EverVoidGameState state)
	{
		super();
		sInstance = this;
		aState = state;
		aGalaxyView = new GalaxyView(aState.getGalaxy());
		changeView(GameViewType.SOLAR, aState.getTempSolarSystem());
	}

	private void changeActiveView(final ClientView view)
	{
		if (aActiveView != null) {
			// TODO: Don't necessarily remove
			delNode(aActiveView);
		}
		aActiveView = view;
		addNode(view);
	}

	private SolarSystemView getSolarSystemView(final SolarSystem ss)
	{
		if (!aSolarViews.containsKey(ss)) {
			aSolarViews.put(ss, new SolarSystemView(ss));
		}
		return aSolarViews.get(ss);
	}

	@Override
	public void onMouseClick(final Vector2f position, final float tpf)
	{
		aActiveView.onMouseClick(position, tpf);
	}

	@Override
	public void onMouseMove(final String name, final float tpf, final Vector2f position)
	{
		aActiveView.onMouseMove(name, tpf, position);
	}

	@Override
	public void onMouseRelease(final Vector2f position, final float tpf)
	{
		aActiveView.onMouseRelease(position, tpf);
	}

	@Override
	public void onMouseWheelDown(final float delta, final float tpf, final Vector2f position)
	{
		aActiveView.onMouseWheelDown(delta, tpf, position);
	}

	@Override
	public void onMouseWheelUp(final float delta, final float tpf, final Vector2f position)
	{
		aActiveView.onMouseWheelUp(delta, tpf, position);
	}

	private void switchView(final GameViewType type, Object arg)
	{
		switch (type) {
			case GALAXY:
				changeActiveView(aGalaxyView);
				break;
			case SOLAR:
				// FIXME: hax
				if (arg == null) {
					arg = aState.getTempSolarSystem();
				}
				changeActiveView(getSolarSystemView((SolarSystem) arg));
				break;
		}
	}
}

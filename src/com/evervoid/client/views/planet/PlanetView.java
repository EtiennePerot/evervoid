package com.evervoid.client.views.planet;

import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.geometry.FrameTimer;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.ComposedView;
import com.evervoid.client.views.EverView;
import com.evervoid.client.views.solar.SolarView;
import com.evervoid.state.prop.Planet;
import com.jme3.math.Vector2f;

public class PlanetView extends ComposedView
{
	private static final float sInnerHeightPercentage = 0.8f;
	private final PlanetBuildingView aBuildings;
	private final Planet aPlanet;
	private final SolarView aSolarView;

	public PlanetView(final SolarView parent, final Planet planet)
	{
		aSolarView = parent;
		aPlanet = planet;
		aBuildings = new PlanetBuildingView(this, planet);
		addView(aBuildings);
	}

	public void close()
	{
		aSolarView.planetViewClose();
	}

	@Override
	public boolean onKeyPress(final KeyboardKey key, final float tpf)
	{
		if (key.equals(KeyboardKey.ESCAPE)) {
			close();
			return true;
		}
		return false;
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		if (super.onLeftClick(position, tpf)) {
			return true;
		}
		// Otherwise, check if click was outside of all subviews bounds
		for (final EverView view : getChildrenViews()) {
			if (view.getBounds().contains(position.x, position.y)) {
				return true; // Still inside
			}
		}
		// Outside of all subviews; close the planet view.
		close();
		return false;
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		final float newY = bounds.y + bounds.height * (1f - sInnerHeightPercentage) / 2f;
		final float newHeight = bounds.height * sInnerHeightPercentage;
		aBuildings.setBounds(new Bounds(bounds.x, newY, bounds.width / 3, newHeight));
	}

	public void slideIn(final float duration)
	{
		aBuildings.slideIn(duration);
	}

	public void slideOut(final float duration, final Runnable callback)
	{
		aBuildings.slideOut(duration);
		if (callback != null) {
			new FrameTimer(callback, duration, 1).start();
		}
	}
}

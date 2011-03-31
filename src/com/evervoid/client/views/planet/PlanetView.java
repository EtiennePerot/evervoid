package com.evervoid.client.views.planet;

import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.ComposedView;
import com.evervoid.state.prop.Planet;

public class PlanetView extends ComposedView
{
	private static final float sInnerHeightPercentage = 0.8f;
	private final PlanetBuildingView aBuildings;
	Planet aPlanet;

	public PlanetView(final Planet planet)
	{
		aPlanet = planet;
		aBuildings = new PlanetBuildingView(this, planet);
		addView(aBuildings);
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
}

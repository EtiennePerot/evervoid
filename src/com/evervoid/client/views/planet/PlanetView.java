package com.evervoid.client.views.planet;

import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.ComposedView;
import com.evervoid.state.prop.Planet;

public class PlanetView extends ComposedView
{
	private final PlanetBuildingView aBuildings;
	Planet aPlanet;

	public PlanetView(final Planet planet)
	{
		aPlanet = planet;
		aBuildings = new PlanetBuildingView(this);
		addView(aBuildings);
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		aBuildings.setBounds(new Bounds(0, 0, bounds.width / 2, bounds.height));
	}
}

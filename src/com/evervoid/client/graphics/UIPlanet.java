package com.evervoid.client.graphics;

import com.evervoid.client.views.solar.SolarSystemGrid;
import com.evervoid.state.prop.Planet;

public class UIPlanet extends UIProp
{
	private final Planet aPlanet;

	public UIPlanet(final SolarSystemGrid grid, final Planet planet)
	{
		super(grid, planet.getLocation());
		aPlanet = planet;
		buildProp();
	}

	@Override
	protected void buildSprite()
	{
		addSprite(new ShadedSprite(aPlanet.getData().getBaseSprite()));
	}
}

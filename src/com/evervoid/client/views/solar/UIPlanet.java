package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.ShadedSprite;
import com.evervoid.state.prop.Planet;

public class UIPlanet extends UIShadedProp
{
	private final Planet aPlanet;

	public UIPlanet(final SolarGrid grid, final Planet planet)
	{
		super(grid, planet.getLocation());
		aPlanet = planet;
		buildProp();
	}

	@Override
	protected void buildSprite()
	{
		final ShadedSprite shade = new ShadedSprite(aPlanet.getData().getBaseSprite());
		addSprite(shade);
		setShade(shade);
	}
}

package com.evervoid.client.graphics;

import com.evervoid.client.views.solar.SolarSystemGrid;
import com.evervoid.state.prop.Planet;
import com.jme3.math.FastMath;

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
		addSprite(aPlanet.getData().getBaseSprite());
		addSprite(new Shade(aPlanet.getData().getBaseSprite()).setShadePortion(0.6f).setGradientPortion(0.5f)
				.setShadeAngle(FastMath.HALF_PI / 2));
	}
}

package com.evervoid.client.graphics;

import com.evervoid.gamedata.Dimension;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.solar.Point;
import com.jme3.math.FastMath;

public class UIPlanet extends UIProp
{
	public UIPlanet(final Grid grid, final Planet planet)
	{
		this(grid, planet.getLocation(), planet.getDimension());
	}

	public UIPlanet(final Grid grid, final Point location, final Dimension size)
	{
		super(grid, new Point(location), size);
	}

	@Override
	protected void buildSprite()
	{
		addSprite("planets/gas/planet_gas_1.png");
		addSprite(new Shade("planets/gas/planet_gas_1.png").setShadePortion(0.6f).setGradientPortion(0.5f)
				.setShadeAngle(FastMath.HALF_PI / 2));
	}
}

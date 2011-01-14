package com.evervoid.client.graphics;

import java.awt.Dimension;

import com.evervoid.client.graphics.geometry.GridPoint;
import com.jme3.math.FastMath;

public class UIPlanet extends UIProp
{
	public UIPlanet(final Grid grid, final GridPoint location, final Dimension size)
	{
		super(grid, location, new Dimension(2, 2));
	}

	@Override
	protected void buildSprite()
	{
		addSprite("planets/gas/planet_gas_1.png");
		addSprite(new Shade("planets/gas/planet_gas_1.png").setShadePortion(0.6f).setGradientPortion(0.5f)
				.setShadeAngle(FastMath.HALF_PI / 2));
	}
}

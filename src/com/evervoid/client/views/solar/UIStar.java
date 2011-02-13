package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.SphericalSprite;
import com.evervoid.state.prop.Star;

public class UIStar extends UIProp
{
	private static final float sRotationSpeedPerUnit = 15;
	private final Star aStar;

	public UIStar(final SolarSystemGrid grid, final Star star)
	{
		super(grid, star.getLocation());
		aStar = star;
		buildProp();
	}

	@Override
	protected void buildSprite()
	{
		final SphericalSprite spr = new SphericalSprite(aStar.getSprite());
		spr.setRotationTime(aStar.getLocation().dimension.getAverageSize() * sRotationSpeedPerUnit);
		addSprite(spr);
	}
}

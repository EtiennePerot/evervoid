package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.SphericalSprite;
import com.evervoid.state.prop.Star;

public class UIStar extends UIProp
{
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
		addSprite(new SphericalSprite("stars/sun_yellow_8x8_2.png").setRotationTime(90));
	}
}

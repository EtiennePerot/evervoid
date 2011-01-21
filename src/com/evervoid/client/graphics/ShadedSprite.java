package com.evervoid.client.graphics;

import com.evervoid.gamedata.OffsetSprite;
import com.jme3.math.FastMath;

public class ShadedSprite extends MultiSprite
{
	Shade aShade;

	public ShadedSprite(final OffsetSprite offSprite)
	{
		super();
		addSprite(offSprite);
		aShade = new Shade(offSprite);
		aShade.setShadePortion(0.6f).setGradientPortion(0.5f).setShadeAngle(FastMath.HALF_PI / 2);
		addSprite(aShade);
	}

	public ShadedSprite(final String image)
	{
		this(new OffsetSprite(image));
	}
}

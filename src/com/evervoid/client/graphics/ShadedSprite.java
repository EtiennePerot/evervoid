package com.evervoid.client.graphics;

import com.evervoid.gamedata.OffsetSprite;

public class ShadedSprite extends MultiSprite implements Shadable
{
	Shade aShade;

	public ShadedSprite(final OffsetSprite offSprite)
	{
		super();
		addSprite(offSprite);
		aShade = new Shade(offSprite);
		aShade.setShadePortion(0.6f).setGradientPortion(0.5f).setShadeAngle(0);
		addSprite(aShade);
	}

	public ShadedSprite(final String image)
	{
		this(new OffsetSprite(image));
	}

	@Override
	public Shadable setGradientPortion(final float gradientPortion)
	{
		aShade.setGradientPortion(gradientPortion);
		return this;
	}

	@Override
	public Shadable setShadeAngle(final float shadeAngle)
	{
		aShade.setShadeAngle(shadeAngle);
		return this;
	}

	@Override
	public Shadable setShadePortion(final float shadePortion)
	{
		aShade.setShadePortion(shadePortion);
		return this;
	}
}

package com.evervoid.client.graphics;

import com.evervoid.state.data.SpriteInfo;
import com.jme3.math.ColorRGBA;

public class ShadedSprite extends MultiSprite implements Shadable
{
	Shade aShade;

	public ShadedSprite(final SpriteInfo offSprite)
	{
		super();
		addSprite(offSprite);
		aShade = new Shade(offSprite);
		aShade.setShadePortion(0.6f).setGradientPortion(0.5f).setShadeAngle(0);
		addSprite(aShade);
	}

	public ShadedSprite(final String image)
	{
		this(new SpriteInfo(image));
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
	public Shadable setShadeColor(final ColorRGBA glowColor)
	{
		aShade.setShadeColor(glowColor);
		return this;
	}

	@Override
	public Shadable setShadePortion(final float shadePortion)
	{
		aShade.setShadePortion(shadePortion);
		return this;
	}
}

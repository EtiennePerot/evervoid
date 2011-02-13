package com.evervoid.client.graphics;

import com.jme3.math.ColorRGBA;

public interface Shadable
{
	public Shadable setGradientPortion(final float gradientPortion);

	public Shadable setShadeAngle(final float shadeAngle);

	public Shadable setShadeColor(final ColorRGBA glowColor);

	public Shadable setShadePortion(final float shadePortion);
}

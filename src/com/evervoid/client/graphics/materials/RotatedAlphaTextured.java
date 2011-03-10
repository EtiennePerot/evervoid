package com.evervoid.client.graphics.materials;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.jme3.math.FastMath;

public class RotatedAlphaTextured extends AlphaTextured
{
	public RotatedAlphaTextured(final String texture) throws TextureException
	{
		super(texture, "RotatedAlphaTextured");
		setRotationAngle(MathUtils.getRandomFloatBetween(0, FastMath.TWO_PI));
	}

	public void setRotationAngle(final float angle)
	{
		setFloat("RotationAngle", MathUtils.mod(angle, FastMath.TWO_PI));
	}
}

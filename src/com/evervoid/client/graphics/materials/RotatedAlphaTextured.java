package com.evervoid.client.graphics.materials;

import com.evervoid.utils.MathUtils;
import com.jme3.math.FastMath;

/**
 * Rotated version of {@link AlphaTextured}. Supports GPU rotation of the texture.
 */
public class RotatedAlphaTextured extends AlphaTextured
{
	/**
	 * Same parameters as {@link AlphaTextured}'s constructor
	 * 
	 * @param texture
	 *            The texture to use
	 * @throws TextureException
	 *             Raised when the texture cannot be found or loaded
	 */
	public RotatedAlphaTextured(final String texture) throws TextureException
	{
		super(texture, "RotatedAlphaTextured");
		setRotationAngle(MathUtils.getRandomFloatBetween(0, FastMath.TWO_PI));
	}

	/**
	 * Set the angle of rotation of the texture
	 * 
	 * @param angle
	 *            The angle of the texture, in radians
	 */
	public void setRotationAngle(final float angle)
	{
		setFloat("RotationAngle", MathUtils.mod(angle, FastMath.TWO_PI));
	}
}

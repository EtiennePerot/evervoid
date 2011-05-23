package com.evervoid.client.graphics.materials;

import com.evervoid.utils.MathUtils;
import com.jme3.math.ColorRGBA;

/**
 * Very simple, plain-color material, with alpha transparency
 */
public class PlainColor extends BaseMaterial
{
	/**
	 * Constructor: Only requires the color to use
	 * 
	 * @param color
	 *            The color of the material
	 */
	public PlainColor(final ColorRGBA color)
	{
		super("PlainColor");
		setColor("Color", color);
	}

	/**
	 * Set the alpha transparency of this material
	 * 
	 * @param alpha
	 *            The alpha transparency to use, from 0 to 1
	 */
	public void setAlpha(final float alpha)
	{
		setBoolean("UseAlphaMultiplier", true);
		setFloat("AlphaMultiplier", MathUtils.clampFloat(0, alpha, 1));
	}

	/**
	 * Change the color of this material
	 * 
	 * @param color
	 *            The new color to use
	 */
	public void setColor(final ColorRGBA color)
	{
		setColor("Color", color);
	}
}

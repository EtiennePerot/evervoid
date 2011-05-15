package com.evervoid.client.graphics;

import com.jme3.math.ColorRGBA;

/**
 * Classes implementing the Shadable interface have setters to control the settings of their shadow.
 */
public interface Shadable
{
	/**
	 * Set the shadow's gradient portion ([0; 1]-width of the gradient part of the shadow)
	 * 
	 * @param gradientPortion
	 *            The width of the gradient part of the shadow
	 * @return This for chainability
	 */
	public Shadable setGradientPortion(final float gradientPortion);

	/**
	 * Set the shadow's angle (in radians)
	 * 
	 * @param shadeAngle
	 *            The angle of the shadow (in radians)
	 * @return This for chainability
	 */
	public Shadable setShadeAngle(final float shadeAngle);

	/**
	 * Set the shadow's color
	 * 
	 * @param glowColor
	 *            The color of the shadow
	 * @return This for chainability
	 */
	public Shadable setShadeColor(final ColorRGBA glowColor);

	/**
	 * Set the shadow's shade portion ([0; 1]-width of the completely-opaque part of the shadow)
	 * 
	 * @param shadePortion
	 *            The width of the shade part of the shadow
	 * @return This for chainability
	 */
	public Shadable setShadePortion(final float shadePortion);
}

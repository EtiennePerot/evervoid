package com.evervoid.client.graphics;

import com.jme3.math.ColorRGBA;

/**
 * Classes implementing the Colorable interface have the capability to be recolored through hue change.
 */
public interface Colorable
{
	/**
	 * Set the hue of this object
	 * 
	 * @param hue
	 *            The hue to apply to the object
	 */
	public void setHue(ColorRGBA hue);

	/**
	 * Set the hue of this object, with a certain intensity
	 * 
	 * @param hue
	 *            The hue to apply to this object
	 * @param multiplier
	 *            The intensity with which the hue should be applied. Values from 0.5 to 2 usually give the best results.
	 */
	public void setHue(ColorRGBA hue, float multiplier);
}

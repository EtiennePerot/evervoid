package com.evervoid.client.graphics;

/**
 * Objects implementing the Spherical interface depict a rotating sphere in space. They have a bunch of setters.
 */
public interface Spherical
{
	/**
	 * Shaves a certain number of rendered pixels off the edge of the sphere
	 * 
	 * @param pixels
	 *            Number of pixels to shave
	 */
	public void setClipPixels(final int pixels);

	/**
	 * Limits the rendered radius of the sphere
	 * 
	 * @param radius
	 *            The radius (from 0 to 1) to render
	 */
	public void setClipRadius(final float radius);

	/**
	 * Set the rotation time of this SphericalSprite, and enables rotation
	 * 
	 * @param time
	 *            The time for a full revolution, in seconds
	 */
	public void setRotationTime(final float time);
}

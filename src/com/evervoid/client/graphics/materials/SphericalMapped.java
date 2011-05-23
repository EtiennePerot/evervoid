package com.evervoid.client.graphics.materials;

import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.client.graphics.Sizable;
import com.evervoid.utils.MathUtils;
import com.jme3.math.Vector2f;

/**
 * Takes a texture and applies a transformation to it that makes it look as if it were wrapped around a globe. Used for the star
 * at the center of each solar system.
 */
public class SphericalMapped extends BaseMaterial implements Sizable
{
	/**
	 * The texture to wrap around the sphere
	 */
	private final BaseTexture aTexture;
	/**
	 * The texture may "rotate" (slide) around the sphere, this holds the "rotation" offset of the texture
	 */
	private float aTextureOffset = 0f;

	/**
	 * Simple constructor
	 * 
	 * @param texture
	 *            The texture to wrap around the sphere
	 * @throws TextureException
	 *             If the texture cannot be found or loaded
	 */
	public SphericalMapped(final String texture) throws TextureException
	{
		super("SphericalMapped");
		aTexture = GraphicManager.getTexture(texture);
		setTexture("ColorMap", aTexture.getTexture());
		setFloat("TextureOffset", 0f);
		setFloat("ClipRadius", 1f);
		setFloat("TexturePortion", aTexture.getHorizontalPortion());
	}

	/**
	 * Add offset to the texture's "slide"/"rotate" effect around the sphere.
	 * 
	 * @param offset
	 *            The amount to slide by from 0 to 2, 2 being a complete revolution of the sphere (thus won't change the
	 *            appearance of the sphere at all). The offset will automatically be wrapped, do not worry about overflows.
	 */
	public void addOffset(final float offset)
	{
		aTextureOffset = (aTextureOffset + offset / 2f) % 1f;
		setFloat("TextureOffset", aTextureOffset);
	}

	/**
	 * @return The dimension of the texture
	 */
	@Override
	public Vector2f getDimensions()
	{
		return aTexture.getDimensions();
	}

	/**
	 * @return The height of the texture
	 */
	@Override
	public float getHeight()
	{
		return aTexture.getHeight();
	}

	/**
	 * @return The <b>DISPLAYED</b> width of the texture, which is half the total width of the texture. It is also equal to the
	 *         height, since this is a sphere.
	 */
	@Override
	public float getWidth()
	{
		// Spherical texture
		return aTexture.getHeight();
	}

	/**
	 * Set the transparency of this material
	 * 
	 * @param alpha
	 *            The transparency to use
	 */
	public void setAlpha(final float alpha)
	{
		setBoolean("UseAlphaMultiplier", true);
		setFloat("AlphaMultiplier", MathUtils.clampFloat(0, alpha, 1));
	}

	/**
	 * Shaves a certain number of rendered pixels off the edge of this sphere
	 * 
	 * @param pixels
	 *            Number of pixels to shave
	 */
	public void setClipPixels(final int pixels)
	{
		setClipRadius(1f - pixels / (getWidth() / 2));
	}

	/**
	 * Limits the rendered radius of the sphere
	 * 
	 * @param radius
	 *            The portion of the radius (from 0 to 1) to render
	 */
	public void setClipRadius(final float radius)
	{
		setFloat("ClipRadius", MathUtils.clampFloat(0f, radius, 1f));
	}
}
